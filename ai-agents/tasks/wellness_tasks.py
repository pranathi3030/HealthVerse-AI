from crewai import Task
from models.schemas import DietGenerateResponse, FitnessGenerateResponse, WellnessGenerateResponse

def generate_nutrition_plan_task(agent, context_data: dict) -> Task:
    return Task(
        description=(
            f"Create a nutrition and hydration plan for a user with the following context:\n"
            f"Goal: {context_data.get('goal', 'General wellness')}\n"
            f"Diet Type: {context_data.get('diet_type', 'Balanced')}\n"
            f"Age: {context_data.get('age', 'Unknown')}\n"
            f"Weight: {context_data.get('weight', 'Unknown')}\n"
            f"Height: {context_data.get('height', 'Unknown')}\n"
            f"Allergies: {context_data.get('allergies', 'None')}\n"
            f"Chronic Conditions: {context_data.get('chronic_conditions', 'None')}\n\n"
            "Provide meal suggestions, specific hydration guidance, and practical daily recommendations."
        ),
        expected_output="JSON containing meal_suggestions, hydration_guidance, and practical_recommendations.",
        agent=agent,
        output_json=DietGenerateResponse
    )

def generate_fitness_plan_task(agent, context_data: dict) -> Task:
    return Task(
        description=(
            f"Create a fitness plan for a user with the following context:\n"
            f"Goal: {context_data.get('goal', 'General fitness')}\n"
            f"Fitness Level: {context_data.get('fitness_level', 'Beginner')}\n"
            f"Limitations: {context_data.get('limitations', 'None')}\n"
            f"Age: {context_data.get('age', 'Unknown')}\n\n"
            "Provide a safe exercise plan, recovery suggestions, and general fitness advice."
        ),
        expected_output="JSON containing exercise_plan, recovery_suggestions, and general_advice.",
        agent=agent,
        output_json=FitnessGenerateResponse
    )

def generate_wellness_plan_task(agent, context_data: dict) -> Task:
    return Task(
        description=(
            f"Create a general wellness plan based on the following user context:\n"
            f"Sleep Quality: {context_data.get('sleep_quality', 'Average')}\n"
            f"Stress Level: {context_data.get('stress_level', 'Average')}\n"
            f"Daily Activity: {context_data.get('daily_activity', 'Moderate')}\n\n"
            "Provide recommendations to improve sleep, manage stress, and build better daily habits."
        ),
        expected_output="JSON containing sleep_recommendations, stress_management, and daily_habits.",
        agent=agent,
        output_json=WellnessGenerateResponse
    )

def predict_wellness_task(agent, history: list) -> Task:
    return Task(
        description=(
            f"Analyze the following historical health and wellness data: {history}\n\n"
            "Calculate meaningful wellness indicators and identify trends (e.g. sleep consistency, activity levels).\n"
            "Identify potential wellness risks or areas requiring attention.\n"
            "Generate an explainable wellness insight. Use safe wording like 'may' or 'could' instead of medical diagnosis.\n"
            "If there is insufficient data (e.g., empty or only 1 day), explicitly state that in the reason and return a neutral trend."
        ),
        expected_output="JSON matching the PredictiveWellnessResponse schema.",
        agent=agent,
        output_json=PredictiveWellnessResponse
    )
