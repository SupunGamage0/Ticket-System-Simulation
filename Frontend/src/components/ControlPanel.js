import React from 'react';

function ControlPanel({ handleStart, handleStop, isRunning, isConfigured }) {
  return (
    <div className="controls">
      <button className="start-button" onClick={handleStart} disabled={isRunning || !isConfigured}>
        Start
      </button>
      <button className="stop-button" onClick={handleStop} disabled={!isRunning}>
        Stop
      </button>
    </div>
  );
}

export default ControlPanel;