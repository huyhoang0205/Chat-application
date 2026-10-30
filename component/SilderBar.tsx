import { Box, IconButton, Typography } from "@mui/material";
import Logo from "./common/Logo";
import ConversationList from "./chat/ConversationList";
import UserProfile from "./common/UserProfile";
import { Add } from "@mui/icons-material";
import { useEffect } from "react";
import { useChatStore } from "@/store/useChatStore";

interface SliderBarProps {
  showSidebar: boolean;
  newConversation : () => void;
}

export default function SliderBar({ showSidebar, newConversation }: SliderBarProps) {

  useEffect(() => {
    useChatStore.getState().fetchConversations();
  }, [])

  return (
    <Box
      sx={{
        width: showSidebar ? 280 : 0,
        backgroundColor: "2f3136",
        borderRight: "1px solid #202225",
        display: "flex",
        flexDirection: "column",
        transition: "width 0.3s",
        overflow: "hidden",
      }}
    >
      <Box
        sx={{
          height: 48,
          px: 2,
          borderBottom: "1px solid #202225",
          display: "flex",
          alignItems: "center",
          gap: 1.5,
        }}
      >
        <Logo size="small" showText={false} />
        <Typography
          sx={{
            color: "#fff",
            fontWeight: 600,
            fontSize: "0.9375rem",
            flex: 1,
          }}
        >
          Tin nhắn
        </Typography>
        <IconButton
          size="small"
          onClick={newConversation}
          sx={{ color: "#b9bbbe", "&:hover": { color: "#fff" } }}
        >
          <Add fontSize="small" />
        </IconButton>
      </Box>

      <Box sx={{ flex: 1, overflowY: "auto" }}>
        <ConversationList
        />
      </Box>

      <Box sx={{ borderTop: "1px solid #202225" }}>
        <UserProfile />
      </Box>
    </Box>
  );
}