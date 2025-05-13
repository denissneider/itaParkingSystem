import { Entity, Column, PrimaryGeneratedColumn } from 'typeorm';

@Entity()
export class Card {
  @PrimaryGeneratedColumn()
  id: number;

  @Column()
  userId: number;

  @Column()
  ime: string;

  @Column()
  stevilka_kartice: string;

  @Column()
  cvc: string;

  @Column()
  veljavnost: string;
}
