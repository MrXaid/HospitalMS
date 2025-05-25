from langchain_google_genai import ChatGoogleGenerativeAI
from langchain_core.prompts import PromptTemplate
from dotenv import load_dotenv
import os

# Load environment variables from .env file
load_dotenv()

# Initialize the Gemini Flash model
llm = ChatGoogleGenerativeAI(
    model="gemini-1.5-flash",
    temperature=0.3,
    google_api_key=os.getenv("GOOGLE_API_KEY"),  # Get API key from environment
    safety_settings={
        "HARM_CATEGORY_HARASSMENT": "BLOCK_NONE",
        "HARM_CATEGORY_HATE_SPEECH": "BLOCK_NONE",
        "HARM_CATEGORY_SEXUALLY_EXPLICIT": "BLOCK_NONE",
        "HARM_CATEGORY_DANGEROUS_CONTENT": "BLOCK_NONE",
    }
)

MEDICAL_PROMPT = """
You are a professional medical assistant. Provide helpful but non-diagnostic advice following these rules:
- List possible conditions (but emphasize this is not a diagnosis)
- Suggest evidence-based prevention tips
- Recommend when to see a doctor
- Format responses in clear bullet points
- Always include a disclaimer that this is not medical advice

User query: {query}
Response:"""

def get_medical_response(query: str) -> str:
    try:
        prompt = PromptTemplate.from_template(MEDICAL_PROMPT)
        chain = prompt | llm
        response = chain.invoke({"query": query}).content
        
        # Ensure response includes disclaimer if not already present
        if "disclaimer" not in response.lower():
            response += "\n\nDisclaimer: This is not medical advice. Please consult a healthcare professional for proper diagnosis."
            
        return response
        
    except Exception as e:
        error_msg = f"AI service error: {str(e)}"
        print(error_msg)  # Log the error for debugging
        return "I'm unable to provide medical advice at the moment. Please try again later."