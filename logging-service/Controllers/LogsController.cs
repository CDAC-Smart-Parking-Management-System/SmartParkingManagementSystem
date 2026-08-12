using LoggingService.Data;
using LoggingService.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace LoggingService.Controllers
{
    // Same idea as @RestController + @RequestMapping("/api/logs") in Spring Boot.
    [ApiController]
    [Route("api/logs")]
    public class LogsController : ControllerBase
    {
        private readonly LoggingDbContext _context;

        public LogsController(LoggingDbContext context)
        {
            _context = context;
        }

        // POST /api/logs
        // Called by the main Spring Boot backend (and the chatbot service)
        // every time something worth recording happens.
        [HttpPost]
        public async Task<ActionResult<LogEntry>> CreateLog(LogEntry log)
        {
            log.Id = 0;                    // let the database generate the Id
            log.CreatedAt = DateTime.UtcNow; // always set on the server, never trust the client

            _context.Logs.Add(log);
            await _context.SaveChangesAsync();

            return Ok(log);
        }

        // GET /api/logs
        // Returns the most recent logs first. Optionally filter by
        // ?serviceName=smart-parking-backend or ?action=BOOKING_CREATED
        [HttpGet]
        public async Task<ActionResult<IEnumerable<LogEntry>>> GetLogs(
            [FromQuery] string? serviceName,
            [FromQuery] string? action)
        {
            var query = _context.Logs.AsQueryable();

            if (!string.IsNullOrWhiteSpace(serviceName))
            {
                query = query.Where(l => l.ServiceName == serviceName);
            }

            if (!string.IsNullOrWhiteSpace(action))
            {
                query = query.Where(l => l.Action == action);
            }

            var logs = await query
                .OrderByDescending(l => l.CreatedAt)
                .Take(200) // simple safety limit, keeps the response small
                .ToListAsync();

            return Ok(logs);
        }

        // GET /api/logs/5
        [HttpGet("{id}")]
        public async Task<ActionResult<LogEntry>> GetLogById(int id)
        {
            var log = await _context.Logs.FindAsync(id);

            if (log == null)
            {
                return NotFound();
            }

            return Ok(log);
        }
    }
}
