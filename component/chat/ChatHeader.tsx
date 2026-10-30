import { useChatStore } from "@/store/useChatStore";
import { Box, Divider, IconButton, Typography } from "@mui/material";
import { Menu as MenuIcon } from "@mui/icons-material";

interface ChatHeaderProps {
  toggleSideBar: () => void;
}

export default function ChatHeader({ toggleSideBar }: ChatHeaderProps) {
  const selectedConvId = useChatStore((state) => state.activeConversationId);
  const selectedConv = useChatStore((state) =>
    state.conversations.find((conv) => conv.id === selectedConvId!),
  );
  return (
    <Box
      sx={{
        display: "flex",
        gap: 2,
        height: 48,
        px: 2,
        alignItems: "center",
        borderBottom: "1px solid #202225",
        backgroundColor: "#36393f",
      }}
    >
      <IconButton onClick={toggleSideBar} sx={{ color: "#b9bbbe" }}>
        <MenuIcon fontSize="small" />
      </IconButton>

      {selectedConv && (
        <>
          <Typography
            sx={{ color: "#fff", fontWeight: 600, fontSize: "0.9375rem" }}
          >
            {selectedConv.name ||
              selectedConv.participantInfo?.map((p) => p.username).join(", ")}
          </Typography>
          <Divider
            orientation="vertical"
            flexItem
            sx={{ borderColor: "#202225" }}
          />
          <Typography sx={{ color: "#b9bbbe", fontSize: "0.8125rem" }}>
            {selectedConv.participantInfo?.length} thành viên
          </Typography>
          <Divider orientation="vertical" flexItem sx={{ mx: 1 }} />
          <Box sx={{ display: "flex", alignItems: "center", gap: 0.75 }}>
            <Box
              sx={{
                width: 10,
                height: 10,
                borderRadius: "50%",
                backgroundColor: "#23a55a",
                border: "2px solid #36393f",
                boxSizing: "border-box",
              }}
            />
            <Typography
              sx={{
                color: selectedConv.isOnline ? "#23a55a" : "#b9bbbe",
                fontWeight: 500,
                fontSize: "0.875rem",
              }}
            >
              {selectedConv.isOnline
                ? "Đang hoạt động"
                : selectedConv.lastOnlineAt || "Không hoạt động"}
            </Typography>
          </Box>
        </>
      )}
    </Box>
  );
}