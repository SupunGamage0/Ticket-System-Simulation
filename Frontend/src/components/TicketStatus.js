import React from 'react';

function TicketStatus({ availableTickets, maxTickets, totalTickets, ticketsReleased }) {
  const percentageFull = maxTickets > 0 ? (availableTickets / maxTickets) * 100 : 0;
  const ticketsRemaining = Math.max(0, totalTickets - ticketsReleased);

  let statusColor = 'green';
  if (percentageFull >= 90) {
    statusColor = 'red';
  } else if (percentageFull >= 70) {
    statusColor = 'yellow';
  }

  const progressBarStyles = {
    width: `${percentageFull}%`,
    backgroundColor: statusColor,
    height: '20px',
    borderRadius: '5px',
    transition: 'width 0.5s ease-in-out, background-color 0.5s ease-in-out',
  };

  const statusTextStyles = {
    color: statusColor,
    fontWeight: 'bold',
    marginTop: '5px',
  };

  return (
    <div className="ticket-status">
      <h2>Ticket Pool Status</h2>
      <div className="progress-bar-container">
        <div className="progress-bar" style={progressBarStyles}></div>
      </div>
      <div style={statusTextStyles}>
        {availableTickets} / {maxTickets} Tickets Available in Pool
      </div>
      <div>Tickets Remaining to be Released: {ticketsRemaining}</div>
      {ticketsReleased >= totalTickets && availableTickets === 0 && (
        <div style={{ color: 'red', fontWeight: 'bold' }}>Sold Out!</div>
      )}
    </div>
  );
}

export default TicketStatus;