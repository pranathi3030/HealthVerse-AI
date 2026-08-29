from crewai import Task
import json
from models.schemas import WellnessPlanResponse

def orchestrate_wellness_plan_task(agent, goal: str, context: dict) -> Task:
    return Task(
        description=(
            f"Create a holistic wellness plan for the user based on their goal: '{goal}'.\n"
            f"User Context: {json.dumps(context)}\n\n"
            "Combine general health guidelines, nutritional advice, and fitness routines "
            "into one comprehensive plan. Ensure the plan is structured correctly and "
            "does not provide medical diagnoses."
        ),
        expected_output="A structured JSON object matching the WellnessPlanResponse schema.",
        agent=agent,
        output_json=WellnessPlanResponse
    )

def orchestrate_query_task(query: str, context: dict) -> Task:
    return Task(
        description=(
            f"Understand the user's request: '{query}'.\n"
            f"Context: {json.dumps(context)}\n\n"
            "1. Identify which specialized agents are relevant to this request.\n"
            "2. Delegate sub-tasks to those agents with only the necessary context.\n"
            "3. Collect their results.\n"
            "4. Combine the results into one coherent, personalized response.\n"
            "Do not provide medical diagnosis."
        ),
        expected_output="A single coherent, personalized response string combining insights from relevant specialized agents.",
    )
