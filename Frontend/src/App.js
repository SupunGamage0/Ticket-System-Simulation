import React, { useState, useEffect } from 'react';
import ConfigurationForm from './components/ConfigurationForm';
import ControlPanel from './components/ControlPanel';
import LogDisplay from './components/LogDisplay';
import TicketStatus from './components/TicketStatus';
import './styles/App.css';

function App() {
  const [formData, setFormData] = useState({
    totalTickets: '',
    ticketReleaseRate: '',
    customerRetrievalRate: '',
    maxTicketCapacity: '',
  });

  const [logs, setLogs] = useState([]);
  const [error, setError] = useState({});
  const [formIsValid, setFormIsValid] = useState(false);
  const [isRunning, setIsRunning] = useState(false);
  const [isConfigured, setIsConfigured] = useState(false);
  const [pool, setPool] = useState([]);
  const [ticketsReleased, setTicketsReleased] = useState(0);

  const validateForm = () => {
    const { totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity } = formData;
    let errors = {};
    let valid = true;

    if (!totalTickets || !ticketReleaseRate || !customerRetrievalRate || !maxTicketCapacity) {
      errors.empty = 'All fields are required.';
      valid = false;
    }

    if (
      isNaN(totalTickets) ||
      isNaN(ticketReleaseRate) ||
      isNaN(customerRetrievalRate) ||
      isNaN(maxTicketCapacity) ||
      totalTickets <= 0 ||
      ticketReleaseRate <= 0 ||
      customerRetrievalRate <= 0 ||
      maxTicketCapacity <= 0
    ) {
      errors.invalid = 'Please enter positive numeric values only.';
      valid = false;
    }

    setError(errors);
    return valid;
  };

  useEffect(() => {
    setFormIsValid(validateForm());
  }, [formData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSave = async () => {
    if (!formIsValid) {
      setLogs((prevLogs) => [...prevLogs, 'Error: Invalid input, please correct the fields.']);
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/configure', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      const data = await response.text();
      console.log('Configuration response:', data);
      setLogs((prevLogs) => [...prevLogs, `Configuration Saved: ${JSON.stringify(formData)}`]);
      setIsConfigured(true);
    } catch (error) {
      console.error('Error saving configuration:', error);
      setLogs((prevLogs) => [...prevLogs, 'Error: Could not save configuration.']);
    }
  };

  const handleStart = async () => {
    if (!isConfigured) {
      setLogs((prevLogs) => [...prevLogs, 'Error: System not configured.']);
      return;
    }

    setIsRunning(true);
    setLogs([]);
    setPool([]);
    setTicketsReleased(0);

    try {
      const response = await fetch('http://localhost:8080/api/logs');
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }

      const initialLogs = await response.json();
      setLogs(initialLogs);
    } catch (error) {
      console.error('Error fetching initial logs:', error);
      setLogs((prevLogs) => [...prevLogs, 'Error: Could not fetch initial logs.']);
    }

    const socket = new WebSocket('ws://localhost:8080/ticket-status');

    socket.onmessage = (event) => {
      const message = JSON.parse(event.data);
      console.log('Message from server:', message);

      setLogs((prevLogs) => [...prevLogs, JSON.stringify(message)]);

      if (message.type === 'vendor') {
        setTicketsReleased((prevTicketsReleased) => prevTicketsReleased + 1);
        setPool((prevPool) => {
          if (prevPool.length < parseInt(formData.maxTicketCapacity, 10)) {
            return [...prevPool, {}];
          } else {
            return prevPool;
          }
        });
      } else if (message.type === 'customer') {
        setPool((prevPool) => {
          if (prevPool.length > 0) {
            return prevPool.slice(0, -1);
          } else {
            return prevPool;
          }
        });
      }
    };

    socket.onclose = () => {
      console.log('WebSocket connection closed');
    };

    socket.onerror = (error) => {
      console.error('WebSocket error:', error);
    };
  };

  const handleStop = () => {
    setIsRunning(false);
    setLogs((prevLogs) => [...prevLogs, 'System Stopped']);
  };

  return (
    <div className="app">
      <ConfigurationForm
        formData={formData}
        handleChange={handleChange}
        handleSave={handleSave}
        error={error}
        formIsValid={formIsValid}
      />

      <TicketStatus
        availableTickets={pool.length}
        maxTickets={formData.maxTicketCapacity}
        totalTickets={formData.totalTickets}
        ticketsReleased={ticketsReleased}
      />

      <ControlPanel
        handleStart={handleStart}
        handleStop={handleStop}
        isRunning={isRunning}
        isConfigured={isConfigured}
      />

      <LogDisplay logs={logs} />
    </div>
  );
}

export default App;