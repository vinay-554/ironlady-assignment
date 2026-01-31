import { useState } from "react";

function App() {
  const [message, setMessage] = useState("");
  const [chat, setChat] = useState([]);
  const [loading, setLoading] = useState(false);

  const sendMessage = async (customMessage) => {
    const finalMessage = customMessage || message;
    if (!finalMessage.trim()) return;

    // Add user message
    setChat((prev) => [...prev, { sender: "user", text: finalMessage }]);
    if (!customMessage) setMessage("");
    setLoading(true);

    try {
      const response = await fetch("http://localhost:8080/api/chat", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ message: finalMessage }),
      });

      const data = await response.text();

      // Add AI response
      setChat((prev) => [...prev, { sender: "ai", text: data }]);
    } catch (error) {
      setChat((prev) => [
        ...prev,
        { sender: "ai", text: "Something went wrong. Please try again." },
      ]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <h2>Iron Lady – AI Guidance Assistant</h2>

      {/* Chat Messages */}
      <div className="chat-box">
        {chat.map((msg, index) => (
          <div
            key={index}
            className={msg.sender === "user" ? "user-msg" : "ai-msg"}
          >
            {msg.text}
          </div>
        ))}

        {loading && <div className="ai-msg">Typing...</div>}
      </div>

      {/* Quick Action Buttons */}
      <div className="quick-actions">
        <button onClick={() => sendMessage("Tell me about Iron Lady programs")}>
          📘 Programs
        </button>

        <button onClick={() => sendMessage("How can I contact Iron Lady?")}>
          📞 Contact Us
        </button>

        <button
          onClick={() =>
            sendMessage("I want personalized leadership guidance")
          }
        >
          🧭 Get Guidance
        </button>
      </div>

      {/* Input Area */}
      <div className="input-area">
        <input
          type="text"
          placeholder="Ask about leadership growth..."
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && sendMessage()}
        />
        <button onClick={() => sendMessage()}>Send</button>
      </div>
    </div>
  );
}

export default App;
