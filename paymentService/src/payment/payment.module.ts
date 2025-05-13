import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { PaymentService } from './payment.service';
import { Card } from '../entities/card.entity';

@Module({
  imports: [TypeOrmModule.forFeature([Card])],
  controllers: [PaymentService],
})
export class PaymentModule {}
