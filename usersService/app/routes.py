from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from passlib.context import CryptContext
from uuid import UUID
from app import user_schemas as schemas, models, database
import logging
from .models import AuditLog
from .database import SessionLocal

# Configure logging
logging.basicConfig(
    filename='users_service.log',
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

router = APIRouter()
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

def get_db():
    db = database.SessionLocal()
    try:
        yield db
    finally:
        db.close()

#POST metoda
@router.post("/users", response_model=schemas.UserOut)
def create_user(user: schemas.UserCreate, db: Session = Depends(get_db)):
    logger.info(f"Attempting to create user with email: {user.email}")
    if db.query(models.User).filter(models.User.email == user.email).first():
        logger.warning(f"User with email {user.email} already exists in the database.")
        raise HTTPException(status_code=400, detail="Email already registered")

    hashed_password = pwd_context.hash(user.password)

    db_user = models.User(
        name=user.name,
        surname=user.surname,
        email=user.email,
        license_plate=user.license_plate,
        phone_number=user.phone_number,
        password=hashed_password
    )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)

    audit = AuditLog(
        action="create_user",
        user_id=db_user.id,
        changed_by="jaz_hehe",
        details={
            "created_user": {
                "name": db_user.name,
                "email": db_user.email
            }
        }
    )
    db.add(audit)
    db.commit()

    logger.info(f"User created with ID: {db_user.id}")
    return db_user

#DELETE metoda
@router.delete("/users/{user_id}", status_code=204)
def delete_user(user_id: UUID, db: Session = Depends(get_db)):
    logger.info(f"Attempting to delete user with ID: {user_id}")
    user = db.query(models.User).filter(models.User.id == str(user_id)).first()
    if not user:
        logger.warning(f"User with ID {user_id} not found")
        raise HTTPException(status_code=404, detail="User not found")

    # Revizijski zapis
    audit = AuditLog(
        action="delete_user",
        user_id=user.id,
        changed_by="jaz_hehe",
        details={"deleted_user": {
            "name": user.name,
            "email": user.email
        }}
    )
    db.add(audit)

    db.delete(user)
    db.commit()
    logger.info(f"User with ID {user_id} successfully deleted")
    return

#GET metoda
@router.get("/users", response_model=list[schemas.UserOut])
def get_users(db: Session = Depends(get_db)):
    logger.info("Fetching all users")
    logger.info(f"GET /users called")
    users = db.query(models.User).all()

    audit = AuditLog(
        action="get_users",
        user_id="N/A",
        changed_by="jaz_hehe",
        details={"info": "User list fetched"}
    )
    db.add(audit)
    db.commit()

    return users