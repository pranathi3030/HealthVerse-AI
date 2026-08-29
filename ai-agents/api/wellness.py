from fastapi import APIRouter, HTTPException
from models.schemas import WellnessGenerateRequest, WellnessGenerateResponse, PredictiveWellnessRequest, PredictiveWellnessResponse
from crews.wellness_crew import run_wellness_crew, run_predictive_wellness_crew

router = APIRouter()

@router.post("/generate", response_model=WellnessGenerateResponse)
def generate_wellness(request: WellnessGenerateRequest):
    try:
        context = request.model_dump()
        result = run_wellness_crew(context)
        if isinstance(result, str):
            import json
            result = json.loads(result)
        return WellnessGenerateResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/predict", response_model=PredictiveWellnessResponse)
def predict_wellness(request: PredictiveWellnessRequest):
    try:
        result = run_predictive_wellness_crew(request.history)
        if isinstance(result, str):
            import json
            result = json.loads(result)
        return PredictiveWellnessResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
