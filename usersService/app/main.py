from ddtrace import patch_all, tracer

patch_all()

tracer.configure(
    settings={
        "SERVICE": "usersService",
    }
)

from fastapi import FastAPI
from app import models, database
from app.routes import router
from app.models import AuditLog
from app.database import SessionLocal

import logging

app = FastAPI()

# Create tables
models.Base.metadata.create_all(bind=database.engine)

@app.on_event("startup")
async def startup_event():
    logging.info("🚀 usersService started")

    db = SessionLocal()
    db.add(AuditLog(
        action="startup",
        user_id="N/A",
        changed_by="system",
        details={"info": "Application started"}
    ))
    db.commit()
    db.close()

@app.on_event("shutdown")
async def shutdown_event():
    logging.info("🛑 usersService stopped")

    db = SessionLocal()
    db.add(AuditLog(
        action="shutdown",
        user_id="N/A",
        changed_by="system",
        details={"info": "Application stopped"}
    ))
    db.commit()
    db.close()

# Include routes
app.include_router(router)