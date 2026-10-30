"use client";
import { Box } from "@mui/material";

import NewConversationDialog from "@/component/chat/NewConversationDiaLog";
import ChatWindow from "@/component/chat/ChatWindow";
import ChatHeader from "@/component/chat/ChatHeader";
import SliderBar from "@/component/SilderBar";
import { useState } from "react";

export default function HomePage() {

  const [showSidebar, setShowSidebar] = useState(true);
  const [showNewConversation, setShowNewConversation] = useState(false);

  return (
    <Box
      sx={{
        display: "flex",
        height: "100vh",
        backgroundColor: "#363933f",
        overflow: "hidden",
      }}
    >
      <SliderBar
        showSidebar={showSidebar}
        newConversation={() => setShowNewConversation(true)}
      />
      {/* main chat */}
      <Box
        sx={{
          flex: 1,
          display: "flex",
          flexDirection: "column",
          overflowY: "auto",
        }}
      >
        {/* chat header */}
        <ChatHeader toggleSideBar={() => setShowSidebar(!showSidebar)} />

        {/* message */}
        <ChatWindow />
      </Box>
      {/* New Conversation */}
      <NewConversationDialog
        open={showNewConversation}
        onClose={() => setShowNewConversation(false)}
      />
    </Box>
  );
}
