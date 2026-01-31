import { useEffect, useState } from "react";

function App() {
  const [leads, setLeads] = useState([]);
  const [form, setForm] = useState({
    name: "",
    email: "",
    phone: "",
    program: "",
  });

  const API_URL = "http://localhost:8081/api/leads";

  // Fetch all leads
  const fetchLeads = async () => {
    const res = await fetch(API_URL);
    const data = await res.json();
    setLeads(data);
  };

  useEffect(() => {
    fetchLeads();
  }, []);

  // Handle form change
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // Add new lead
  const addLead = async () => {
    if (!form.name || !form.email) return;

    await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(form),
    });

    setForm({ name: "", email: "", phone: "", program: "" });
    fetchLeads();
  };

  // Update status
  const updateStatus = async (id, status) => {
    await fetch(`${API_URL}/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ status }),
    });
    fetchLeads();
  };

  // Delete lead
  const deleteLead = async (id) => {
    await fetch(`${API_URL}/${id}`, { method: "DELETE" });
    fetchLeads();
  };

  return (
    <div className="container">
      <h2>Iron Lady – Lead Management</h2>

      {/* Add Lead Form */}
      <div className="form">
        <input
          name="name"
          placeholder="Name"
          value={form.name}
          onChange={handleChange}
        />
        <input
          name="email"
          placeholder="Email"
          value={form.email}
          onChange={handleChange}
        />
        <input
          name="phone"
          placeholder="Phone"
          value={form.phone}
          onChange={handleChange}
        />
        <select
          name="program"
          value={form.program}
          onChange={handleChange}
        >
          <option value="">Select Program</option>
          <option value="Leadership Accelerator">Leadership Accelerator</option>
          <option value="Career Restart">Career Restart</option>
          <option value="Executive Mentorship">Executive Mentorship</option>
        </select>

        <button onClick={addLead}>Add Lead</button>
      </div>

      {/* Leads Table */}
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Program</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {leads.map((lead) => (
            <tr key={lead.id}>
              <td>{lead.name}</td>
              <td>{lead.email}</td>
              <td>{lead.phone}</td>
              <td>{lead.program}</td>
              <td>
                  <span className={`status ${lead.status.toLowerCase()}`}>
                    {lead.status}
                  </span>
              </td>
              <td>
              <div className="action-buttons">
                <button
                  className="btn contacted"
                  disabled={lead.status !== "NEW"}
                  onClick={() => updateStatus(lead.id, "CONTACTED")}
                >
                  Contacted
                </button>

                <button
                  className="btn enrolled"
                  disabled={lead.status === "ENROLLED"}
                  onClick={() => updateStatus(lead.id, "ENROLLED")}
                >
                  Enrolled
                </button>

                <button
                  className="btn delete"
                  onClick={() => deleteLead(lead.id)}
                >
                  Delete
                </button>
              </div>
            </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default App;
