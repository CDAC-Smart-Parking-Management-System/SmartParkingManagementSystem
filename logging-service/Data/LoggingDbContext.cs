using Microsoft.EntityFrameworkCore;
using LoggingService.Models;

namespace LoggingService.Data
{
    // This DbContext points ONLY at logging.db (SQLite).
    // It knows nothing about the main project's MySQL database -
    // that is the whole point of each microservice owning its own data.
    public class LoggingDbContext : DbContext
    {
        public LoggingDbContext(DbContextOptions<LoggingDbContext> options) : base(options)
        {
        }

        public DbSet<LogEntry> Logs { get; set; }
    }
}
