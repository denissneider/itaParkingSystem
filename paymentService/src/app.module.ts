import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PaymentModule } from './payment/payment.module';
import { Card } from './entities/card.entity';
import * as dotenv from 'dotenv';
dotenv.config();


@Module({
  imports: [
  /*  //delo lokalno za testiranje
  TypeOrmModule.forRoot({
      type: 'postgres',
      //host: 'payment-db', // docker #
      host: 'localhost',    //lokalno
      //port: 5432,         // docker #
      port: 5434,           //lokalno      
      username: 'user',
      password: 'password',
      database: 'payment_db',
      entities: [Card],
      synchronize: true,
    }),
    */

    //za zaganjanje v Dockerju so tu env variabli
    TypeOrmModule.forRoot({
      type: 'postgres',
      host: process.env.DB_HOST,
      port: Number(process.env.DB_PORT),
      username: process.env.DB_USERNAME,
      password: process.env.DB_PASSWORD,
      database: process.env.DB_NAME,
      entities: [Card],
      synchronize: true,
    }),
    

    PaymentModule,
  ],
})
export class AppModule {}
