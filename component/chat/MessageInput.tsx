'use client'
import {Box, IconButton, TextField} from '@mui/material'
import {Send, AddCircleOutline} from '@mui/icons-material'
import { useState } from 'react'

interface MessageInputProps {
    onSend: (content: string) => void
    disabled?: boolean
}

export default function MessageInput({
    onSend,
    disabled = false
} : MessageInputProps) {

    const [message, setMessage] = useState('');

    const handleSend = () => {
        if(message.trim() && !disabled) {
            onSend(message.trim());
            setMessage('');
        }
    }

    const handleKeyDown = (e: React.KeyboardEvent) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            handleSend()
        }
    }
    return (
        <Box sx={{p:2 ,backgroundColor: '#36393f'}}>
           <Box sx={{
                display: 'flex',
                gap: 1,
                alignItems: 'center',
                backgroundColor: '#40444b',
                borderRadius: 2,
                px: 1.5,
                py: 0.5
            }}>
                <IconButton size='small' sx={{color: '#b9bbbe'}}>
                    <AddCircleOutline fontSize='small'/>
                </IconButton>
                <TextField
                    fullWidth 
                    multiline
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    maxRows={4}
                    placeholder='Nhập tin nhắn...'
                    variant='standard'
                    onKeyDown={handleKeyDown}
                    disabled={disabled}
                    sx={{
                        '& .MuiInput-root': {
                        color: '#dcddde',
                        fontSize: '0.9375rem',
                        '&:before, &:after': {
                            display: 'none',
                        },
                        },
                        '& .MuiInputBase-input': {
                        padding: '8px 0',
                        '&::placeholder': {
                            color: '#72767d',
                            opacity: 1,
                        },
                        },
                    }}
                />
                <IconButton
                    onClick={handleSend}
                    disabled={!message.trim() || disabled}
                    size="small"
                    sx={{
                        color: message.trim() ? '#5865f2' : '#72767d',
                        '&:hover': {
                            color: '#4752c4',
                        },
                    }}  
                >
                    <Send fontSize='small' />
                </IconButton>
           </Box>
        </Box>
    )
}