from fastapi import APIRouter, HTTPException
from models.schemas import DietGenerateRequest, DietGenerateResponse
from crews.wellness_crew import run_nutrition_crew

router = APIRouter()

@router.post("/generate", response_model=DietGenerateResponse)
def generate_diet(request: DietGenerateRequest):
    try:
        context = request.model_dump()
        result = run_nutrition_crew(context)
        if isinstance(result, str):
            import json
            result = json.loads(result)
        return DietGenerateResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
