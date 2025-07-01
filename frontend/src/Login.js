import React, { useState } from "react";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();

    // Ví dụ check tạm user/pass cứng (sau bạn thay bằng API gọi backend)
    if (username === "admin" && password === "123456") {
      setMessage("Login successful!");
    } else {
      setMessage("Invalid username or password");
    }
  };

  return (
    <div style={{ maxWidth: 300, margin: "50px auto", fontFamily: "Arial, sans-serif" }}>
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <div style={{ marginBottom: 12 }}>
          <label>Username:</label><br />
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            style={{ width: "100%", padding: 8 }}
            required
          />
        </div>
        <div style={{ marginBottom: 12 }}>
          <label>Password:</label><br />
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            style={{ width: "100%", padding: 8 }}
            required
          />
        </div>
        <button type="submit" style={{ padding: "8px 12px" }}>Login</button>
      </form>
      {message && <p style={{ marginTop: 12, color: message.includes("successful") ? "green" : "red" }}>{message}</p>}
    </div>
  );
}
