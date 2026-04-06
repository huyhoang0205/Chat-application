'use client'
import {Box, Typography, IconButton, Divider} from '@mui/material'
import {Add, Menu as MenuIcon} from '@mui/icons-material'
import Logo from '@/component/common/Logo'


import UserProfile from '@/component/common/UserProfile'
import NewConversationDialog from '@/component/chat/NewConversationDiaLog'
import MessageList from '@/component/chat/MessageList'
import MessageInput from '@/component/chat/MessageInput'
import { useEffect, useState } from 'react'
import ConversationList from '@/component/chat/ConversationList'
import ChatPlaceholder from '@/component/chat/ChatPlaceholder'
import {useAuthGuard} from '@/hook/useAuthGuard'
import { ChatMessageResponse, ConversationDetailResponse, ConversationType, MessageType, StatusResponse } from '@/type'
import { conversationService } from '@/service/conversation.service'
import { messageService } from '@/service/message.service'
import { useWebSocket } from '@/hook/useWebSocket'
import { API_ENDPOINTS } from '@/api/apiEnpoints'
import {useConnectWebSocket} from '@/hook/useConnectWebSocket'

export default function HomePage() {

  const {isAuthenticated} = useAuthGuard();

  const [conversation, setConversations] = useState<ConversationDetailResponse[]>([])
  const [message, setMessages] = useState<ChatMessageResponse[]>([])
  const [selectedConversation, setSelectedConversation] = useState<string |null>(null)
  const [loadingConversations, setLoadingConversations] = useState(false)
  const [loadingMessage, setLoadingMessages] = useState(false)
  const [sendingMessage, setSendingMessage] = useState(false)
  const [showSidebar, setShowSidebar] = useState(true);
  const [showNewConversation , setShowNewConversation] = useState(false)
  const [updateStatus, setUpdateStatus] = useState<boolean | null>(null);


  const {connected} = useConnectWebSocket()

  useWebSocket(API_ENDPOINTS.WEBSOCKET.MESSAGE, (message: ChatMessageResponse ) => {
    console.log("message:::",message)
    if(selectedConversation && message.conversationId === selectedConversation) {
      setMessages((prev) => [...prev,message])
    } 
    const conversationId = message.conversationId;
    // const index = conversation.findIndex(item => item.id === conversationId)
    // const newConversation = [...conversation]
    // const [item ] = newConversation.splice(index, 1)
    // newConversation.unshift(item);
    // setConversations(newConversation)
    pushConvToTop(conversationId)
  })

  useWebSocket(API_ENDPOINTS.WEBSOCKET.STATUS, (message: StatusResponse) => {
      console.log("/topic/status --- message:::: ",message)
      const isUpdateStatus = conversation.flatMap(item => item.participantInfo).flatMap(participant => participant.userId)
      .includes(message.userId);
      if(isUpdateStatus) {
          setUpdateStatus(true);
      }
  })


  useEffect(() => {
    if(isAuthenticated) {
      loadConversations()

    }
  }, [isAuthenticated, updateStatus])

  useEffect(() => {
    if(selectedConversation) {
      loadMessages(selectedConversation);
    }
  }, [selectedConversation])

  const loadConversations = async () => {
    try {
      setLoadingConversations(true)
      const response = await conversationService.getMyConversations(1,50)
      setConversations(response.data.content);
    }catch (error) {
      console.error('Failed to load conversation :::',error)
    }finally {
      setLoadingConversations(false)
      setUpdateStatus(false)
    }
  }
  const loadMessages = async (conversationId : string) => {
    try{
        setLoadingMessages(true);
        const response = await messageService.getMessages(conversationId, 1, 20);
        setMessages(response.data.content.reverse());
    } catch(error) {
    console.error('Failed to load message:::', error)
    } finally {
      setLoadingMessages(false);
    }
  } 

  const handleSendMessage = async (content: string) => {
    if(!selectedConversation) return;
    const tempId = `temp-${crypto.randomUUID}`

    try {
      setSendingMessage(true)

      const response = await messageService.sendMessage({
        conversationId: selectedConversation,
        content,
        messageType: MessageType.TEXT,
        tempId,
      })

      setMessages((prev) => [...prev, response.data])
      pushConvToTop(selectedConversation)

    } catch(error) {
      console.error('Failed to send message:::', error)
    } finally {
      setSendingMessage(false)
    }
  }

  const handleCOnversationCreated = (conversationId: string) => {
    loadConversations();
    setSelectedConversation(conversationId)
  }

  const pushConvToTop = (conversationId: string) => {
      const index = conversation.findIndex(item => item.id === conversationId)
      const newConversation = [...conversation]
      const [item ] = newConversation.splice(index, 1)
      newConversation.unshift(item);
      setConversations(newConversation)
  }

  const selectedConv = conversation.find(c => c.id === selectedConversation);

  if(!isAuthenticated) {
    return null;
  }

  return (
    <Box sx={{display: 'flex', height: '100vh', backgroundColor: '#363933f', overflow:'hidden'}}>
      <Box
        sx={{
          width: showSidebar ? 280 : 0,
          backgroundColor: "2f3136",
          borderRight: '1px solid #202225',
          display: 'flex',
          flexDirection: 'column',
          transition: 'width 0.3s',
          overflow: 'hidden'
        }}
      >
        <Box sx={{
          height: 48,
          px: 2,
          borderBottom: "1px solid #202225",
          display: 'flex',
          alignItems:'center',
          gap: 1.5
        }}>
            <Logo size='small' showText= {false} />
            <Typography sx={{color: '#fff', fontWeight: 600, fontSize:'0.9375rem', flex:1}}>
              Tin nhắn
            </Typography>
            <IconButton 
              size='small'
              onClick={() => setShowNewConversation(true)}
              sx={{color: '#b9bbbe', '&:hover': {color: '#fff'}}} 
            >
              <Add fontSize='small'/>
            </IconButton>
        </Box>

        <Box sx={{flex:1 , overflowY: 'auto'}}>
          <ConversationList
            conversations={conversation}
            selectedId={selectedConversation}
            onSelect={setSelectedConversation}
            loading={loadingConversations}
          />
        </Box>


        <Box sx={{borderTop: '1px solid #202225'}}>
          <UserProfile />
        </Box>
      </Box>
      {/* main chat */}
      <Box sx={{flex:1, display: 'flex', flexDirection : 'column', overflowY: 'auto' }}>
        {/* chat header */}
        <Box 
        sx={{
          display: 'flex',
          gap:2,
          height: 48,
          px:2,
          alignItems: 'center',
          borderBottom: '1px solid #202225',
          backgroundColor: '#36393f'
        }}>
          <IconButton
            onClick={() => setShowSidebar(!showSidebar)} 
            sx={{color: '#b9bbbe',}}
          >
            <MenuIcon fontSize='small'/>
          </IconButton>

          {selectedConv &&
          (
          <>
            <Typography sx={{color: '#fff', fontWeight: 600,fontSize:'0.9375rem'}}>
              {selectedConv.name || selectedConv.participantInfo?.map(p => p.username).join(', ')}
            </Typography>
            <Divider orientation='vertical' flexItem sx={{borderColor:'#202225'}} />
            <Typography sx={{color:'#b9bbbe', fontSize: '0.8125rem'}}>
              {selectedConv.participantInfo?.length} thành viên
            </Typography>
            <Divider orientation='vertical' flexItem sx={{mx:1}}/>
            <Box sx={{display: 'flex', alignItems:'center', gap: 0.75}}>
              <Box sx={{
                width: 10,
                height: 10,
                borderRadius: '50%',
                backgroundColor: '#23a55a',
                border: '2px solid #36393f',
                boxSizing: 'border-box'
              }}/>
              <Typography sx={{color: selectedConv.isOnline ? '#23a55a' : '#b9bbbe', fontWeight: 500, fontSize: '0.875rem'}}>
                {selectedConv.isOnline ? 'Đang hoạt động' : selectedConv.lastOnlineAt || 'Không hoạt động'}
              </Typography>
            </Box>
          </>
          )
          }
        </Box>

        {/* message */}
        {selectedConversation ? (
          <>
            <MessageList messages = {message} loading={loadingMessage}/>
            <MessageInput 
              onSend={handleSendMessage}
              disabled = {sendingMessage}
            />
          </>
        ) : (
          <>
            <ChatPlaceholder varian='no-conversation'/>
          </>
        )
      }
      </Box>
        {/* New Conversation */}
        <NewConversationDialog 
          open = {showNewConversation}
          onClose ={() => setShowNewConversation(false)}
          onConversationCreated={handleCOnversationCreated}
        />
    </Box>
  );
}
