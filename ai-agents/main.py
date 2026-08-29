import os
from pathlib import Path
from dotenv import load_dotenv

# Load environment variables FIRST, before any other imports
BASE_DIR = Path(__file__).resolve().parent
load_dotenv(BASE_DIR / ".env")

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from api import health, report, diet, fitness, wellness, chat, planning

app = FastAPI(title="HealthVerse AI Agent Service", version="1.0.0")

# Allow existing frontend and backend to communicate with the service
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/health")
async def health_check():
    return {"status": "ok", "service": "HealthVerse AI Agent Service"}

# Include routers
app.include_router(health.router, prefix="/api/v1/agents/health", tags=["Health Analyzer"])
app.include_router(report.router, prefix="/api/v1/agents/report", tags=["Report Analyzer"])
app.include_router(diet.router, prefix="/api/v1/agents/diet", tags=["Nutrition Agent"])
app.include_router(fitness.router, prefix="/api/v1/agents/fitness", tags=["Fitness Agent"])
app.include_router(wellness.router, prefix="/api/v1/agents/wellness", tags=["Wellness Agent"])
app.include_router(chat.router, prefix="/api/v1/agents", tags=["Chat Agent"])
app.include_router(planning.router, prefix="/api/v1/agents/planning", tags=["Planning Agent"])

# Alias routes to satisfy the prompt architecture requirement without breaking existing Spring Boot integrations
app.include_router(health.router, prefix="/ai/symptoms", tags=["Health Analyzer Alias"])
app.include_router(report.router, prefix="/ai/report", tags=["Report Analyzer Alias"])
app.include_router(planning.router, prefix="/ai/wellness-plan", tags=["Planning Agent Alias"])
app.include_router(chat.router, prefix="/ai", tags=["Chat Agent Alias"])  # chat.router already maps to /chat


if __name__ == "__main__":
    import uvicorn
    from config.settings import settings
    uvicorn.run(app, host=settings.HOST, port=settings.PORT)
