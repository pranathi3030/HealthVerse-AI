from fastapi import APIRouter, HTTPException
from models.schemas import ChatRequest, ChatResponse
from crews.chat_crew import run_chat_crew

router = APIRouter()

@router.post("/chat", response_model=ChatResponse)
def chat(request: ChatRequest):
    try:
        result = run_chat_crew(request.query, request.context)
        if isinstance(result, str):
            import json
            result = json.loads(result)
        return ChatResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
