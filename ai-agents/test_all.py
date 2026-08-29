from fastapi.testclient import TestClient
from main import app
import pytest

client = TestClient(app)

def test_health_check():
    response = client.get("/health")
    assert response.status_code == 200
    assert response.json() == {"status": "ok", "service": "HealthVerse AI Agent Service"}

def test_health_analyze():
    response = client.post(
        "/api/v1/agents/health/analyze",
        json={"symptoms": "fever, headache and tiredness", "age": 21}
    )
    assert response.status_code == 200
    data = response.json()
    assert "severity" in data
    assert "insights" in data
    assert "recommendations" in data
    assert "seekProfessionalCare" in data

def test_report_analyze():
    sample_report = "Patient John Doe. Fasting Glucose 120 mg/dL (Normal: 70-100 mg/dL). Cholesterol 210 mg/dL."
    response = client.post(
        "/api/v1/agents/report/analyze",
        json={"report_text": sample_report}
    )
    assert response.status_code == 200
    data = response.json()
    assert "summary" in data
    assert "extracted_values" in data
    assert "abnormal_values" in data
    assert "wellness_recommendations" in data

def test_diet_generate():
    response = client.post(
        "/api/v1/agents/diet/generate",
        json={"goal": "weight management", "diet_type": "balanced"}
    )
    assert response.status_code == 200
    data = response.json()
    assert "meal_suggestions" in data
    assert "hydration_guidance" in data
    assert "practical_recommendations" in data

def test_fitness_generate():
    response = client.post(
        "/api/v1/agents/fitness/generate",
        json={"goal": "improve cardiovascular health", "fitness_level": "beginner"}
    )
    assert response.status_code == 200
    data = response.json()
    assert "exercise_plan" in data
    assert "recovery_suggestions" in data
    assert "general_advice" in data

def test_wellness_generate():
    response = client.post(
        "/api/v1/agents/wellness/generate",
        json={"sleep_quality": "poor", "stress_level": "high", "daily_activity": "low"}
    )
    assert response.status_code == 200
    data = response.json()
    assert "sleep_recommendations" in data
    assert "stress_management" in data
    assert "daily_habits" in data

def test_chat():
    response = client.post(
        "/api/v1/agents/chat",
        json={"query": "How can I improve my daily health and sleep?", "context": {}}
    )
    assert response.status_code == 200
    data = response.json()
    assert "response" in data
    assert "disclaimer" in data
    assert "recommend_professional" in data
