from fastapi import APIRouter, HTTPException
from models.schemas import FitnessGenerateRequest, FitnessGenerateResponse
from crews.wellness_crew import run_fitness_crew

router = APIRouter()

@router.post("/generate", response_model=FitnessGenerateResponse)
def generate_fitness(request: FitnessGenerateRequest):
    try:
        context = request.model_dump()
        result = run_fitness_crew(context)
        if isinstance(result, str):
            import json
            result = json.loads(result)
        return FitnessGenerateResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
