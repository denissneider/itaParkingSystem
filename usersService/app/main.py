from fastapi import FastAPI
from app import models, database
from app.routes import router

app = FastAPI()

# Create tables
models.Base.metadata.create_all(bind=database.engine)

# Include routes
app.include_router(router)