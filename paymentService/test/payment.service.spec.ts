// test/payment.service.spec.ts
import { Test, TestingModule } from '@nestjs/testing';
import { PaymentService } from '../src/payment/payment.service';
import { getRepositoryToken } from '@nestjs/typeorm';
import { Card } from '../src/entities/card.entity';
import { Repository, DeleteResult } from 'typeorm';

describe('PaymentService', () => {
  let service: PaymentService;
  let cardRepo: jest.Mocked<Repository<Card>>;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      controllers: [PaymentService],
      providers: [
        {
          provide: getRepositoryToken(Card),
          useValue: {
            findOne: jest.fn(),
            create: jest.fn(),
            save: jest.fn(),
            delete: jest.fn(),
          },
        },
      ],
    }).compile();

    service = module.get<PaymentService>(PaymentService);
    cardRepo = module.get(getRepositoryToken(Card));
  });

  it('should log success if payment matches saved card', async () => {
    const spy = jest.spyOn(console, 'log').mockImplementation();

    cardRepo.findOne.mockResolvedValue({
      userId: 1,
      stevilka_kartice: '4111111111111111',
      cvc: '123',
      veljavnost: '12/26',
    } as Card);

    await service.handlePayment({
      userId: 1,
      reservationId: 'RES-001',
      amount: 20,
      stevilka_kartice: '4111111111111111',
      cvc: '123',
      veljavnost: '12/26',
    });

    expect(spy).toHaveBeenCalledWith(
      expect.stringContaining('Payment successful')
    );

    spy.mockRestore();
  });

  it('should save a new card', async () => {
    const spy = jest.spyOn(console, 'log').mockImplementation();
    cardRepo.create.mockReturnValue({} as Card);

    await service.handleAddCard({
      userId: 2,
      ime: 'Test User',
      stevilka_kartice: '4000000000000002',
      cvc: '123',
      veljavnost: '12/30',
    });

    expect(cardRepo.create).toHaveBeenCalled();
    expect(cardRepo.save).toHaveBeenCalled();

    spy.mockRestore();
  });

  it('should delete a card', async () => {
    const spy = jest.spyOn(console, 'log').mockImplementation();
    cardRepo.delete.mockResolvedValue({ affected: 1 } as DeleteResult);

    await service.handleRemoveCard({ userId: 2 });

    expect(cardRepo.delete).toHaveBeenCalledWith({ userId: 2 });
    spy.mockRestore();
  });
});
