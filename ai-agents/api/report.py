from fastapi import APIRouter, HTTPException, UploadFile, File
from models.schemas import ReportAnalyzeRequest, ReportAnalyzeResponse
from crews.report_crew import run_report_crew
import PyPDF2
import io

router = APIRouter()

@router.post("/analyze", response_model=ReportAnalyzeResponse)
def analyze_report(request: ReportAnalyzeRequest):
    try:
        result = run_report_crew(request.report_text)
        if isinstance(result, str):
            import json
            result = json.loads(result)
        return ReportAnalyzeResponse(**result)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.post("/upload", response_model=ReportAnalyzeResponse)
async def analyze_report_pdf(file: UploadFile = File(...)):
    if not file.filename.lower().endswith('.pdf'):
        raise HTTPException(status_code=400, detail="Only PDF files are supported")
    
    try:
        content = await file.read()
        pdf_reader = PyPDF2.PdfReader(io.BytesIO(content))
        extracted_text = ""
        for page in pdf_reader.pages:
            extracted_text += page.extract_text() + "\n"
            
        if not extracted_text.strip():
            # Fallback to OCR for scanned PDFs
            try:
                import pytesseract
                from pdf2image import convert_from_bytes
                
                images = convert_from_bytes(content)
                for image in images:
                    extracted_text += pytesseract.image_to_string(image) + "\n"
            except Exception as e:
                print(f"OCR fallback failed (ensure Poppler and Tesseract are installed): {e}")

        if not extracted_text.strip():
            raise HTTPException(status_code=400, detail="Could not extract any text from the PDF. It might be scanned/image-based, and OCR is either not installed or failed.")
        result = run_report_crew(extracted_text)
        if isinstance(result, str):
            import json
            result = json.loads(result)
        return ReportAnalyzeResponse(**result)
    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"PDF processing failed: {str(e)}")
