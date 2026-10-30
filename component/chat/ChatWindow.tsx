import { useChatStore } from "@/store/useChatStore";
import ChatPlaceholder from "./ChatPlaceholder";
import MessageList from "./MessageList";
import { useEffect } from "react";
import MessageInput from "./MessageInput";

export default function ChatWindow() {
    const activeConversationId = useChatStore((state) => state.activeConversationId);
   
    useEffect(() => {
      if (!activeConversationId) return;
      useChatStore.getState().fetchMessages(activeConversationId);
    }, [activeConversationId, ]);

    if(!activeConversationId) {
        return <ChatPlaceholder varian="no-conversation"/>
    }

    return (
      <>
        <MessageList />
        <MessageInput />
      </>
    );
}