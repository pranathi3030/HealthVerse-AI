from crewai import Task
from models.schemas import ChatResponse

def chat_task(agent, query: str, context: dict = None) -> Task:
    context_str = str(context) if context else "None"
    return Task(
        description=(
            f"Answer the user's health query clearly and empathetically.\n"
            f"Query: {query}\n"
            f"User Context: {context_str}\n\n"
            "Respond naturally but include a standard medical disclaimer. "
            "If the query sounds like a serious medical issue, set recommend_professional to true."
        ),
        expected_output="JSON containing response, disclaimer, and recommend_professional flag.",
        agent=agent,
        output_json=ChatResponse
    )
