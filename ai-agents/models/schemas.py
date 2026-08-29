from pydantic import BaseModel, Field
from typing import List, Optional, Dict, Any

# ==========================================
# HEALTH / SYMPTOM ANALYZER
# ==========================================

class HealthAnalyzeRequest(BaseModel):
    symptoms: str
    age: Optional[int] = None
    weight: Optional[float] = None
    height: Optional[float] = None
    allergies: Optional[List[str]] = None
    chronic_conditions: Optional[List[str]] = None

class HealthAnalyzeResponse(BaseModel):
    symptoms_understood: str = Field(description="Summary of the symptoms understood from the user")
    possible_general_causes: List[str] = Field(description="List of possible general causes for these symptoms")
    risk_level: str = Field(description="The assessed risk level (e.g., low, moderate, high)")
    self_care_guidance: List[str] = Field(description="Self-care advice and guidance")
    warning_signs: List[str] = Field(description="Warning signs to watch out for")
    when_to_seek_professional_help: str = Field(description="Clear advice on when to consult a doctor")

# ==========================================
# MEDICAL REPORT ANALYZER
# ==========================================

class ReportAnalyzeRequest(BaseModel):
    report_text: str

class ReportAnalyzeResponse(BaseModel):
    report_summary: str = Field(description="A simple, easily understandable summary of the medical report")
    important_observations: List[str] = Field(description="Important observations from the report")
    abnormal_values: List[str] = Field(description="List of values that are outside the normal range")
    simple_explanation: str = Field(description="A simple explanation of what the results mean")
    wellness_guidance: List[str] = Field(description="Wellness recommendations based on the findings")
    when_to_consult_doctor: str = Field(description="Clear advice on when to consult a doctor regarding these results")

# ==========================================
# NUTRITION / DIET AGENT
# ==========================================

class DietGenerateRequest(BaseModel):
    goal: str
    diet_type: str
    age: Optional[int] = None
    weight: Optional[float] = None
    height: Optional[float] = None
    allergies: Optional[List[str]] = None
    chronic_conditions: Optional[List[str]] = None

class DietGenerateResponse(BaseModel):
    meal_suggestions: List[str] = Field(description="Personalized meal suggestions")
    hydration_guidance: str = Field(description="Specific guidance on water intake")
    practical_recommendations: List[str] = Field(description="Practical dietary habits and recommendations")

# ==========================================
# FITNESS / WELLNESS AGENT
# ==========================================

class FitnessGenerateRequest(BaseModel):
    goal: str
    fitness_level: str = "beginner"
    limitations: Optional[str] = None
    age: Optional[int] = None

class FitnessGenerateResponse(BaseModel):
    exercise_plan: List[str] = Field(description="Recommended exercises and activity plan")
    recovery_suggestions: List[str] = Field(description="Suggestions for rest and recovery")
    general_advice: List[str] = Field(description="General fitness guidance")

class WellnessGenerateRequest(BaseModel):
    sleep_quality: str
    stress_level: str
    daily_activity: str

class WellnessGenerateResponse(BaseModel):
    sleep_recommendations: List[str] = Field(description="Tips for improving sleep")
    stress_management: List[str] = Field(description="Tips for managing stress")
    daily_habits: List[str] = Field(description="Recommended daily wellness habits")

# ==========================================
# HEALTH CONVERSATIONAL AGENT
# ==========================================

class ChatRequest(BaseModel):
    query: str
    context: Optional[Dict[str, Any]] = None

class ChatResponse(BaseModel):
    response: str = Field(description="The conversational response to the user's query")
    disclaimer: str = Field(description="A standard disclaimer about not being a medical professional")
    recommend_professional: bool = Field(description="True if the user's query suggests they need real medical help")

# ==========================================
# PLANNING / ORCHESTRATOR AGENT
# ==========================================

class WellnessPlanRequest(BaseModel):
    goal: str
    context: Optional[Dict[str, Any]] = None

class WellnessPlanResponse(BaseModel):
    executive_summary: str = Field(description="A brief summary of the holistic wellness plan")
    nutrition_plan: List[str] = Field(description="Dietary and nutrition recommendations")
    fitness_plan: List[str] = Field(description="Exercise and activity recommendations")
    health_guidance: List[str] = Field(description="General health and wellness tips")
    daily_schedule: List[str] = Field(description="A suggested daily routine combining all aspects")

class OrchestrateRequest(BaseModel):
    query: str
    context: Optional[Dict[str, Any]] = None

class OrchestrateResponse(BaseModel):
    response: str
    agents_involved: List[str]

class PredictiveWellnessRequest(BaseModel):
    history: List[Dict[str, Any]]

class PredictiveWellnessResponse(BaseModel):
    area: str = Field(description="Area of wellness, e.g., Sleep, Activity")
    trend: str = Field(description="Trend status, e.g., Needs Attention, Improving")
    reason: str = Field(description="Reason for the trend based on data")
    recommendation: str = Field(description="Actionable recommendation")
    confidence: str = Field(description="Confidence level based on available data size")
    contributing_factors: List[str] = Field(description="Factors contributing to this trend")
    evidence: List[str] = Field(description="Data points used as evidence")
