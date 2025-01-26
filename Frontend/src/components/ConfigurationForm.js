import React from 'react';

function ConfigurationForm({ formData, handleChange, handleSave, error, formIsValid }) {
  return (
    <div className="config-container">
      <h2>Configuration Settings</h2>
      {error.empty && <div className="error-message">{error.empty}</div>}
      {error.invalid && <div className="error-message">{error.invalid}</div>}

      <label>
        Total Tickets:
        <input
          type="number"
          name="totalTickets"
          value={formData.totalTickets}
          onChange={handleChange}
          className={error.empty || error.invalid ? 'error' : ''}
        />
      </label>
      <label>
        Ticket Release Rate (tickets per second):
        <input
          type="number"
          name="ticketReleaseRate"
          value={formData.ticketReleaseRate}
          onChange={handleChange}
          className={error.empty || error.invalid ? 'error' : ''}
        />
      </label>
      <label>
        Customer Retrieval Rate (tickets per second):
        <input
          type="number"
          name="customerRetrievalRate"
          value={formData.customerRetrievalRate}
          onChange={handleChange}
          className={error.empty || error.invalid ? 'error' : ''}
        />
      </label>
      <label>
        Maximum Ticket Capacity:
        <input
          type="number"
          name="maxTicketCapacity"
          value={formData.maxTicketCapacity}
          onChange={handleChange}
          className={error.empty || error.invalid ? 'error' : ''}
        />
      </label>

      <button onClick={handleSave} disabled={!formIsValid}>
        Save Configuration
      </button>
    </div>
  );
}

export default ConfigurationForm;