import { Controller, Logger } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { EventPattern, Payload } from '@nestjs/microservices';
import { Repository } from 'typeorm';
import { Card } from '../entities/card.entity';

@Controller()
export class PaymentService {
  private readonly logger = new Logger(PaymentService.name);

  constructor(
    @InjectRepository(Card)
    private readonly cardRepository: Repository<Card>,
  ) {}

  @EventPattern('payment_queue')
  async handlePayment(@Payload() data: any) {
    const { userId, reservationId, amount, stevilka_kartice, cvc, veljavnost } = data;

    if (!userId || !reservationId || typeof amount !== 'number' ||
        !stevilka_kartice || !cvc || !veljavnost) {
      this.logger.error('❌ Invalid payment payload:', JSON.stringify(data));
      return;
    }

    const savedCard = await this.cardRepository.findOne({ where: { userId } });

    if (!savedCard) {
      this.logger.warn(`❌ Payment failed: no card on file for user ${userId}`);
      return;
    }

    const cardMatch =
      savedCard.stevilka_kartice === stevilka_kartice &&
      savedCard.cvc === cvc &&
      savedCard.veljavnost === veljavnost;

    if (!cardMatch) {
      this.logger.warn(`❌ Payment failed: card details do not match for user ${userId}`);
      return;
    }

    if (amount > 0) {
      this.logger.log(`✅ Payment successful for reservation ${reservationId} (amount: ${amount})`);
    } else {
      this.logger.warn(`❌ Payment failed for reservation ${reservationId} (invalid amount: ${amount})`);
    }
  }

  @EventPattern('add_card')
  async handleAddCard(@Payload() data: any) {
    const { userId, ime, stevilka_kartice, cvc, veljavnost } = data;

    if (!userId || !ime || !stevilka_kartice || !cvc || !veljavnost) {
      this.logger.error('❌ Invalid card data:', JSON.stringify(data));
      return;
    }

    const card = this.cardRepository.create({ userId, ime, stevilka_kartice, cvc, veljavnost });
    await this.cardRepository.save(card);
    this.logger.log(`💳 Card saved for user ${userId}: **** ${stevilka_kartice.slice(-4)}`);
  }

  @EventPattern('remove_card')
  async handleRemoveCard(@Payload() data: any) {
    const { userId } = data;

    if (!userId) {
      this.logger.error('❌ Missing userId in remove_card event');
      return;
    }

    const deleted = await this.cardRepository.delete({ userId });
    if (deleted.affected && deleted.affected > 0) {
      this.logger.log(`🗑️ Card deleted for user ${userId}`);
    } else {
      this.logger.warn(`⚠️ No card found to delete for user ${userId}`);
    }
  }
}
