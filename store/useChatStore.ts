import { conversationService } from "@/service/conversation.service";
import { messageService } from "@/service/message.service";
import {
  ChatMessageResponse,
  ConversationDetailResponse,
  MessageType,
  PresenceEvent,
  ConversationType,
} from "@/type";
import { create } from "zustand";

interface ChatState {
  // Lưu tin nhắn theo từng cuộc hội thoại: { [conversationId]: Message[] }
  conversations: ConversationDetailResponse[];
  messagesByConversation: Record<string, ChatMessageResponse[]>;
  activeConversationId: string | null;

  isLoadingConversations: boolean;
  isLoadingMessages: boolean;
  isSendingMessage: boolean;

  fetchConversations: () => Promise<void>;
  addConversations: (conversation: ConversationDetailResponse) => void;
  updateStatusInConversations: (event: PresenceEvent) => void;

  setActiveConversation: (id: string) => void;

  fetchMessages: (conversationId: string) => Promise<void>;
  addMessage: (conversationId: string, message: ChatMessageResponse) => void;
  setMessages: (
    conversationId: string,
    messages: ChatMessageResponse[],
  ) => void;
  sendMessage: (content: string) => Promise<void>;
}

export const useChatStore = create<ChatState>((set, get) => ({
  conversations: [],
  messagesByConversation: {},
  activeConversationId: null,

  isLoadingConversations: false,
  isLoadingMessages: false,
  isSendingMessage: false,

  fetchConversations: async () => {
    set({ isLoadingConversations: true });
    try {
      const conversationsData = await conversationService.getMyConversations(
        1,
        50,
      );
      set({ conversations: conversationsData.data.content });
    } finally {
      set({ isLoadingConversations: false });
    }
  },

  addConversations: (conversation: ConversationDetailResponse) => {
    set((state) => ({
      conversations: [conversation, ...state.conversations],
    }));
  },

  updateStatusInConversations: (event) => {
    const newConversations = get().conversations.map((conv) => {
      if (
        conv.conversationType === ConversationType.PRIVATE &&
        conv.participantInfo.some((info) => info.userId === event.userId)
      ) {
        return {
          ...conv,
          isOnline: !conv.isOnline
        }
      }
      return conv
    });
    set({
      conversations: newConversations
    })
  },

  setActiveConversation: (id) => set({ activeConversationId: id }),

  addMessage: (conversationId, message) =>
    set((state) => {
      const currentMessages =
        state.messagesByConversation[conversationId] || [];

      const targetConv = state.conversations.find(
        (conv) => conv.id === conversationId,
      );

      const updateConvs: ConversationDetailResponse[] = targetConv
        ? [
            {
              ...targetConv,
              lastMessageId: message.id,
              lastMessageContent: message.content,
              lastMessageTime: message.createdAt,
            },
            ...state.conversations.filter((conv) => conv.id !== conversationId),
          ]
        : state.conversations;
      return {
        messagesByConversation: {
          ...state.messagesByConversation,
          [conversationId]: [...currentMessages, message],
        },
        conversations: updateConvs,
      };
    }),

  setMessages: (conversationId, messages) =>
    set((state) => ({
      messagesByConversation: {
        ...state.messagesByConversation,
        [conversationId]: messages,
      },
    })),

  fetchMessages: async (conversationId: string) => {
    set({ isLoadingMessages: true });
    try {
      const messages = await messageService.getMessages(conversationId);

      get().setMessages(conversationId, messages.data.content);
    } catch (error) {
      console.error("Lỗi tải lịch sử chat:", error);
    } finally {
      set({ isLoadingMessages: false });
    }
  },
  sendMessage: async (content) => {
    const selectedConversationId = get().activeConversationId;
    const tempId = `temp-${crypto.randomUUID}`;
    set({isSendingMessage: true})
    try {
      const newMessage = await messageService.sendMessage({
        conversationId: selectedConversationId!,
        content,
        messageType: MessageType.TEXT,
        tempId,
      });

      set((state) => {
        const currentMessages =
          state.messagesByConversation[selectedConversationId!] || [];

        const targetConv = state.conversations.find(
          (conv) => conv.id === selectedConversationId,
        );

        const updateConvs: ConversationDetailResponse[] = targetConv
          ? [
              {
                ...targetConv,
                lastMessageId: newMessage.data.id,
                lastMessageContent: newMessage.data.content,
                lastMessageTime: newMessage.data.createdAt,
              },
              ...state.conversations.filter(
                (conv) => conv.id !== selectedConversationId,
              ),
            ]
          : state.conversations;

        return {
          messagesByConversation: {
            ...state.messagesByConversation,
            [selectedConversationId!]: [...currentMessages, newMessage.data],
          },
          conversations: updateConvs,
        };
      });
    } catch (error) {
      console.error("Lỗi khi gửi tin nhắn", error);
    } finally {
      set({isSendingMessage: false})
    }
  },
}));
