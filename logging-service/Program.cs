using LoggingService.Data;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

// 1) Register MVC controllers (same idea as @RestController in Spring Boot)
builder.Services.AddControllers();

// 2) Register Swagger, so you get a browsable API page at /swagger,
//    just like springdoc-openapi does for the main Java backend.
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// 3) Register the DbContext, pointing at the logging_db MySQL database.
//    This is a SEPARATE database from the main project's smart_parking_db.
var connectionString = builder.Configuration.GetConnectionString("LoggingDb");

builder.Services.AddDbContext<LoggingDbContext>(options =>
    options.UseMySql(connectionString, ServerVersion.AutoDetect(connectionString)));

// 4) Allow the React frontend (and anything else) to call this API
//    directly from the browser during development.
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAll", policy =>
    {
        policy.AllowAnyOrigin().AllowAnyMethod().AllowAnyHeader();
    });
});

var app = builder.Build();

// 5) Make sure the logging_db database and the Logs table exist.
//    EnsureCreated() is the simplest option for a student project -
//    it creates the database/tables from the model on first run,
//    no EF migrations needed.
using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<LoggingDbContext>();
    db.Database.EnsureCreated();
}

app.UseSwagger();
app.UseSwaggerUI();

app.UseCors("AllowAll");

app.MapControllers();

app.Run();
