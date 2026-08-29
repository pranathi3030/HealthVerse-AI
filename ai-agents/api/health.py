from fastapi import APIRouter, HTTPException
from models.schemas import HealthAnalyzeRequest, HealthAnalyzeResponse
from crews.health_crew import run_health_crew

router = APIRouter()

@router.post("/analyze", response_model=HealthAnalyzeResponse)
def analyze_health(request: HealthAnalyzeRequest):
    try:
        context = request.model_dump()
        result = run_health_crew(context)
        # Ensure it conforms to our pydantic model in case CrewAI returns raw dict
        if isinstance(result, str):
            import json
            result = json.loads(result)
        return HealthAnalyzeResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
