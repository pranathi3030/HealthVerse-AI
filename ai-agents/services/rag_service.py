import os
# pyrefly: ignore [missing-import]
from langchain_community.document_loaders import DirectoryLoader, TextLoader
# pyrefly: ignore [missing-import]
from langchain_text_splitters import RecursiveCharacterTextSplitter
# pyrefly: ignore [missing-import]
from langchain_huggingface import HuggingFaceEmbeddings
# pyrefly: ignore [missing-import]
from langchain_community.vectorstores import Chroma

# Paths
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DATA_DIR = os.path.join(BASE_DIR, "data", "knowledge_base")
PERSIST_DIR = os.path.join(BASE_DIR, "data", "chroma_db")

# Global singleton for the vector store
_vectorstore = None

def get_vectorstore():
    global _vectorstore
    if _vectorstore is not None:
        return _vectorstore

    # Ensure the data directory exists
    if not os.path.exists(DATA_DIR):
        os.makedirs(DATA_DIR)

    # Initialize the HuggingFace embeddings
    # all-MiniLM-L6-v2 is a small, fast, local embedding model
    embeddings = HuggingFaceEmbeddings(model_name="all-MiniLM-L6-v2")

    if os.path.exists(PERSIST_DIR) and os.listdir(PERSIST_DIR):
        # Load existing vector store
        _vectorstore = Chroma(persist_directory=PERSIST_DIR, embedding_function=embeddings)
    else:
        # Load documents and create the vector store
        print(f"Loading documents from {DATA_DIR}...")
        loader = DirectoryLoader(DATA_DIR, glob="**/*.txt", loader_cls=TextLoader)
        documents = loader.load()

        if not documents:
            print("No documents found in knowledge base. Vector store will be empty.")
            _vectorstore = Chroma(persist_directory=PERSIST_DIR, embedding_function=embeddings)
            return _vectorstore

        # Split texts
        text_splitter = RecursiveCharacterTextSplitter(chunk_size=500, chunk_overlap=50)
        chunks = text_splitter.split_documents(documents)

        # Create Chroma vector store
        _vectorstore = Chroma.from_documents(
            documents=chunks,
            embedding=embeddings,
            persist_directory=PERSIST_DIR
        )
        print(f"Vector store initialized with {len(chunks)} chunks.")

    return _vectorstore

def search_medical_knowledge(query: str, top_k: int = 3) -> str:
    """
    Searches the local vector store for information relevant to the query.
    Returns a formatted string of the results.
    """
    try:
        vs = get_vectorstore()
        results = vs.similarity_search(query, k=top_k)
        if not results:
            return "No relevant information found in the medical knowledge base."
        
        context = []
        for i, doc in enumerate(results):
            context.append(f"Source [{i+1}]: {doc.page_content}")
            
        return "\n\n".join(context)
    except Exception as e:
        return f"Error searching knowledge base: {str(e)}"
