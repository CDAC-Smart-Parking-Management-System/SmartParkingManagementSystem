using System.ComponentModel.DataAnnotations;

namespace LoggingService.Models
{
    // This is the "table" that gets stored in the logging microservice's
    // own SQLite database (logging.db). It has NOTHING to do with the
    // main project's MySQL database - it is a completely separate
    // database, which is exactly what a microservice is supposed to have.
    public class LogEntry
    {
        [Key]
        public int Id { get; set; }

        // which application/service sent this log
        // e.g. "smart-parking-backend"
        [Required]
        public string ServiceName { get; set; } = string.Empty;

        // short code for what happened
        // e.g. "BOOKING_CREATED", "VEHICLE_CHECK_IN"
        [Required]
        public string Action { get; set; } = string.Empty;

        // human readable description of the event
        [Required]
        public string Message { get; set; } = string.Empty;

        // the user this event relates to (optional)
        public string? UserEmail { get; set; }

        // set automatically by the server, never sent by the client
        public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
    }
}
