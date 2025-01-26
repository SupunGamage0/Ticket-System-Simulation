import React from "react";

function LogDisplay({ logs }) {
    return (
        <div className="logs-container">
            <h2>Logs</h2>
            <div className="logs">
                {logs.map((log, index) => (
                    <div key={index}>{log}</div>
                ))}
            </div>
        </div>
    );
}

export default LogDisplay;