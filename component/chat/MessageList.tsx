'use client';

import { Box, Avatar, Typography, CircularProgress } from '@mui/material';
import { ChatMessageResponse } from '@/type';
import { format, isToday, isYesterday } from 'date-fns';
import { vi } from 'date-fns/locale';
import { useUserStore } from '@/store/useUserStore';

interface MessageListProps {
  messages: ChatMessageResponse[];
  loading?: boolean;
}

export default function MessageList({ messages, loading = false }: MessageListProps) {
  const { user } = useUserStore();

  const formatMessageTime = (dateString: string) => {
    const date = new Date(dateString);
    if (isToday(date)) {
      return format(date, 'HH:mm', { locale: vi });
    } else if (isYesterday(date)) {
      return `Hôm qua ${format(date, 'HH:mm', { locale: vi })}`;
    }
    return format(date, 'dd/MM HH:mm', { locale: vi });
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100%' }}>
        <CircularProgress sx={{ color: '#5865f2' }} />
      </Box>
    );
  }

  if (messages.length === 0) {
    return (
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          height: '100%',
          color: '#72767d',
        }}
      >
        <Typography variant="body2">Chưa có tin nhắn nào</Typography>
      </Box>
    );
  }

  return (
    <Box
      sx={{
        flex: 1,
        overflowY: 'auto',
        px: 3,
        py: 2,
        display: 'flex',
        flexDirection: 'column',
        '&::-webkit-scrollbar': {
          width: '6px',
        },
        '&::-webkit-scrollbar-track': {
          background: 'transparent',
        },
        '&::-webkit-scrollbar-thumb': {
          background: '#1a1b1e',
          borderRadius: '3px',
          '&:hover': {
            background: '#2e3035',
          },
        },
      }}
    >
      {messages.map((message, index) => {
        const isOwn = message.senderId === user?.userId;
        const prevMessage = messages[index - 1];
        const showAvatar = !prevMessage || prevMessage.senderId !== message.senderId;

        return (
          <Box
            key={message.id}
            sx={{
              display: 'flex',
              gap: 1,
              mb: showAvatar ? 2 : 0.5,
              justifyContent: isOwn ? 'flex-end' : 'flex-start',
              alignItems: 'flex-end',
            }}
          >
            {!isOwn && (
              <Box sx={{ width: 32, height: 32, flexShrink: 0 }}>
                {showAvatar && (
                  <Avatar
                    sx={{
                      bgcolor: '#43b581',
                      width: 32,
                      height: 32,
                      fontSize: '0.875rem',
                      fontWeight: 600,
                    }}
                  >
                    {message.senderName[0]?.toUpperCase()}
                  </Avatar>
                )}
              </Box>
            )}

            <Box
              sx={{
                maxWidth: '65%',
                display: 'flex',
                flexDirection: 'column',
                alignItems: isOwn ? 'flex-end' : 'flex-start',
              }}
            >
              {showAvatar && (
                <Typography
                  sx={{
                    color: isOwn ? '#5865f2' : '#43b581',
                    fontWeight: 600,
                    fontSize: '0.8125rem',
                    mb: 0.5,
                    px: 1.5,
                  }}
                >
                  {message.senderName}
                </Typography>
              )}

              <Box
                sx={{
                  position: 'relative',
                  backgroundColor: isOwn ? '#5865f2' : '#40444b',
                  color: '#fff',
                  px: 2,
                  py: 1.25,
                  borderRadius: '16px',
                  boxShadow: '0 1px 2px rgba(0, 0, 0, 0.15)',
                  '&:hover .message-time': {
                    opacity: 1,
                  },
                }}
              >
                <Typography
                  sx={{
                    fontSize: '0.9375rem',
                    lineHeight: 1.5,
                    wordBreak: 'break-word',
                    whiteSpace: 'pre-wrap',
                  }}
                >
                  {message.content}
                </Typography>

                {message.createdAt && (
                  <Typography
                    className="message-time"
                    sx={{
                      position: 'absolute',
                      bottom: -20,
                      [isOwn ? 'right' : 'left']: 8,
                      color: '#72767d',
                      fontSize: '0.6875rem',
                      opacity: 0,
                      transition: 'opacity 0.2s',
                    }}
                  >
                    {formatMessageTime(message.createdAt)}
                  </Typography>
                )}
              </Box>
            </Box>

            {isOwn && (
              <Box sx={{ width: 32, height: 32, flexShrink: 0 }}>
                {showAvatar && (
                  <Avatar
                    sx={{
                      bgcolor: '#5865f2',
                      width: 32,
                      height: 32,
                      fontSize: '0.875rem',
                      fontWeight: 600,
                    }}
                  >
                    {message.senderName[0]?.toUpperCase()}
                  </Avatar>
                )}
              </Box>
            )}
          </Box>
        );
      })}
    </Box>
  );
}
