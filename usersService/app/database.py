from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, declarative_base
import os
from dotenv import load_dotenv

load_dotenv()  # Load .env file

DATABASE_URL = os.getenv("DATABASE_URL", "postgresql://user:password@users-db:5432/users_db")
#DATABASE_URL = os.getenv("DATABASE_URL", "postgresql://user:password@users-db:5432/users_db")
engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()

#za zaganjanje testov v GitHub Actionih
def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()