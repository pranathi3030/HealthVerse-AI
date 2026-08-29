from fastapi import APIRouter, HTTPException
from models.schemas import WellnessPlanRequest, WellnessPlanResponse, OrchestrateRequest, OrchestrateResponse
from crews.planning_crew import run_planning_crew, run_orchestration

router = APIRouter()

@router.post("/generate", response_model=WellnessPlanResponse)
def generate_wellness_plan(request: WellnessPlanRequest):
    try:
        context = request.context or {}
        result = run_planning_crew(request.goal, context)
        if isinstance(result, str):
            import json
            result = json.loads(result)
        return WellnessPlanResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/orchestrate", response_model=OrchestrateResponse)
def orchestrate_request(request: OrchestrateRequest):
    try:
        context = request.context or {}
        result = run_orchestration(request.query, context)
        return OrchestrateResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
