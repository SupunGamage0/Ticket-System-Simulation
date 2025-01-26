import { useState, useEffect } from "react";

export default function useTicketPool(initialTickets, releaseRate) {
  const [ticketPoolStatus, setTicketPoolStatus] = useState({
    availableTickets: initialTickets,
    maxTickets: initialTickets,
  });

  useEffect(() => {
    const releaseInterval = setInterval(() => {
      setTicketPoolStatus((prevStatus) => {
        const newAvailableTickets = Math.max(
          prevStatus.availableTickets - releaseRate,
          0
        );
        return { ...prevStatus, availableTickets: newAvailableTickets };
      });
    }, 1000);

    return () => clearInterval(releaseInterval); // Clean up the interval on unmount
  }, [releaseRate]);

  return ticketPoolStatus;
}
