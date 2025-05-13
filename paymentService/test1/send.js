const amqp = require('amqplib');

async function send() {
  const conn = await amqp.connect('amqp://localhost');
  const ch = await conn.createChannel();

  const payload = {
    pattern: 'payment_queue',
    data: {
      msg: 'hello from send.js'
    }
  };

  ch.publish('', 'payment_queue', Buffer.from(JSON.stringify(payload)));
  console.log('📤 Sent message via routing key: payment_queue');
  await ch.close();
  await conn.close();
}

send();
