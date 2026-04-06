import {Box, Container} from '@mui/material'
import {ReactNode} from 'react'
import Logo from '@/component/common/Logo'

interface AuthLayoutProps {
    children: ReactNode
}

export default function AuthLayout({children} : AuthLayoutProps) {
    return (
        <>
            <Box
                sx={{
                    minHeight: '100vh',
                    pt: 2
                }}
            >
                <Box sx={{
                    pb: 2,
                    pl: 1.5
                }}>
                    <Logo size='medium' showText={true}/>
                </Box>
                <Container maxWidth = 'sm'>
                    <Box
                        sx={{
                            backgroundColor: '#36393f',
                            borderRadius: 2,
                            p: 4,
                            boxShadow: '0 8px 16px rgba(0,0,0,0.3)'
                        }}
                    >
                        {children}
                    </Box>
                </Container>
            </Box>
        </> 
    )
}