export interface ReportObservation {
  name: string;
  value: string;
  status: 'normal' | 'attention' | 'critical';
  description?: string;
}

export interface ReportAnalysis {
  id: string;
  reportTitle: string;
  dateAnalyzed: string;
  summary: string;
  simpleExplanation: string;
  observations: ReportObservation[];
  recommendations: string[];
  seekProfessionalCare: boolean;
  professionalCareMessage?: string;
}
