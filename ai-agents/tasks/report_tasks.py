from crewai import Task
from models.schemas import ReportAnalyzeResponse

def extract_report_data_task(agent, report_text: str) -> Task:
    return Task(
        description=(
            f"Extract key values, test names, and reference ranges from the following medical report:\n\n"
            f"{report_text}\n\n"
            "Identify any abnormal values accurately without inventing data."
        ),
        expected_output="A detailed summary of the extracted values and identified abnormalities.",
        agent=agent
    )

def interpret_health_context_task(agent) -> Task:
    return Task(
        description=(
            "Review the extracted medical report data provided by the Report Agent. "
            "Interpret the health context in plain language and note any significant health risks."
        ),
        expected_output="A plain-language interpretation of the health risks and context.",
        agent=agent
    )

def generate_report_wellness_task(agent) -> Task:
    return Task(
        description=(
            "Based on the health context and the original extracted report data, generate a final structured "
            "wellness report. Provide a report summary, important observations, abnormal values, "
            "a simple explanation, wellness guidance, and advice on when to consult a doctor."
        ),
        expected_output="JSON containing report_summary, important_observations, abnormal_values, simple_explanation, wellness_guidance, and when_to_consult_doctor.",
        agent=agent,
        output_json=ReportAnalyzeResponse
    )
