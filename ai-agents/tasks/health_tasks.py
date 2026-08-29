from crewai import Task
from models.schemas import HealthAnalyzeResponse
import json

def analyze_symptoms_task(agent, context_data: dict) -> Task:
    return Task(
        description=(
            f"Analyze the following user symptoms and context safely:\n"
            f"Symptoms: {context_data.get('symptoms', 'None')}\n"
            f"Age: {context_data.get('age', 'Unknown')}\n"
            f"Weight: {context_data.get('weight', 'Unknown')}\n"
            f"Height: {context_data.get('height', 'Unknown')}\n"
            f"Allergies: {context_data.get('allergies', 'None')}\n"
            f"Chronic Conditions: {context_data.get('chronic_conditions', 'None')}\n\n"
            "Identify the symptoms understood, possible general causes, risk level, and provide self-care guidance. "
            "Highlight any warning signs and provide clear advice on when to seek professional medical help."
        ),
        expected_output="JSON containing symptoms_understood, possible_general_causes, risk_level, self_care_guidance, warning_signs, and when_to_seek_professional_help.",
        agent=agent,
        output_json=HealthAnalyzeResponse
    )
