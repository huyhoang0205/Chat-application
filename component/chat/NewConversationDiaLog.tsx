'use client'

import {
    Box,
    Dialog,
    DialogTitle,
    DialogContent,
    TextField,
    IconButton
} from '@mui/material'
import {
    Close,
    Search
} from '@mui/icons-material'
import UserSearchList from '@/component/chat/UserSearchList'
import { useEffect, useState } from 'react';
import { ConversationType, UserDetailResponse } from '@/type';
import { useDebounce } from '@/hook/useDebounce';
import { userService } from '@/service/user.service';
import { conversationService } from '@/service/conversation.service';

interface NewConversationDialogProps {
    open: boolean;
    onClose: () => void;
    onConversationCreated: (conversationId: string) => void
}


export default function NewConversationDialog({
    open,
    onClose,
    onConversationCreated
} : NewConversationDialogProps) {
    
    const [keyword, setKeyword] = useState('')
    const [users, setUsers] = useState<UserDetailResponse[]>([])
    const [loading, setLoading] = useState(false)
    const [creating, setCreating] = useState(false)

    const debouncedKeyword = useDebounce(keyword, 500)

    useEffect(() => {
        const searchUsers = async () => {
            if(!debouncedKeyword.trim()) {
                setUsers([]);
                return;
            }

            try {
                setLoading(true)
                const response = await userService.searchUsers(debouncedKeyword,1 ,10);
                setUsers(response.data.data.content)
            } catch (error) {
                console.error('Failed to search users:::', error);
            } finally {
                setLoading(false)
            }
        }

        searchUsers();
    }, [debouncedKeyword])
    
    const handleSelectedUser = async (user: UserDetailResponse) => {
        try {
            setCreating(true);
            const response = await conversationService.createConversation({
                participantIds: [user.userId],
                conversationType: ConversationType.PRIVATE
            })
            onConversationCreated(response.data.id)
            handleClose();
        } catch (error) {
            console.error('Failed to create conversation:::', error)
        } finally {
            setCreating(false);
        }

    }

    const handleClose = () => {
        setKeyword('')
        setUsers([])
        onClose();
    }
    
    return (
        <Dialog
            open= {open}
            onClose={handleClose}
            maxWidth = 'sm'
            fullWidth
            slotProps={{
                paper: {
                    sx: {
                        backgroundColor: '#36393f',
                        backgroundImage: 'none'
                    }
                }
            }}
            
        >
            <DialogTitle
            sx={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                color: '#fff',
                pb: 1,
                fontWeight: 600
            }}>
                Tìm kiếm người dùng
                <IconButton onClick={handleClose} size='small' sx={{color:'#b9bbbe'}}>
                    <Close />
                </IconButton>
            </DialogTitle>
            <DialogContent sx={{pt: 2}}>
                <TextField 
                    fullWidth
                    placeholder='Tìm theo email hoặc tên người dùng...'
                    autoFocus
                    value={keyword}
                    onChange={(e) => setKeyword(e.target.value)}
                    slotProps={{
                        input: {
                            startAdornment: <Search sx={{color:'#72767d,mr:1,'}}/>
                        }
                    }}
                    sx={{
                        '& .MuiOutlinedInput-root': {
                            backgroundColor: '#202225',
                            color: '#fff',
                            '& fieldset': {
                                borderColor: '#202225',
                            },
                            '&:hover fieldset': {
                                borderColor: '#40444b',
                            },
                            '&.Mui-focused fieldset': {
                                borderColor: '#5865f2',
                            },
                        },
                        '& .MuiInputBase-input::placeholder': {
                            color: '#72767d',
                            opacity: 1,
                        },
                        "& input:-webkit-autofill": {
                            WebkitBoxShadow: "0 0 0 100px #202225 inset", 
                            WebkitTextFillColor: "#fff",
                            transition: "background-color 5000s ease-in-out 0s",
                        },
                    }}
                />
            </DialogContent>
            <Box sx={{mt:2, minHeight: 200, maxHeight: 400, overflowY: 'auto'}}>
                <UserSearchList 
                    users = {users}
                    loading = {loading}
                    keyword={keyword}
                    creating={creating}
                    onSelectUser={handleSelectedUser}
                />
            </Box>
        </Dialog>
    )
}


