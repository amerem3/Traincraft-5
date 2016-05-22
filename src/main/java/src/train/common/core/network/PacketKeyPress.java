package src.train.common.core.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import src.train.common.api.EntityRollingStock;
import src.train.common.api.Locomotive;
import src.train.common.entity.digger.EntityRotativeDigger;
import src.train.common.entity.zeppelin.AbstractZeppelin;

/**
 * Sent to the server when a key is pressed on the client.<p>
 * 
 * Values and their meanings as adapted from old source code:<br>
 * 		7 = R<br>
 * 		4 = W<br>
 * 		5 = S<br>
 * 		8 = H<br>
 * 		9 = F<br>
 * 		0 = Y<br>
 * 		1 = A<br>
 * 		2 = X<br>
 * 		3 = D<br>
 * 		6 = C<br>
 */
public class PacketKeyPress implements IMessage {

	/** The key that was pressed. */
	int key;

	public PacketKeyPress() {}

	public PacketKeyPress(int key) {

		this.key = key;
	}

	@Override
	public void fromBytes(ByteBuf bbuf) {

		this.key = bbuf.readInt();
	}

	@Override
	public void toBytes(ByteBuf bbuf) {

		bbuf.writeInt(this.key);
	}

	public static class Handler implements IMessageHandler<PacketKeyPress, IMessage> {

		@Override
		public IMessage onMessage(PacketKeyPress message, MessageContext context) {
			System.out.print("Packet Key Pressed");

			Entity ridingEntity = context.getServerHandler().playerEntity.ridingEntity;

			/* "instanceof" is null-safe, but we check to avoid four unnecessary instanceof checks for when the value is null anyways. */
			if (ridingEntity != null) {

				if (ridingEntity instanceof Locomotive) {

					((Locomotive) ridingEntity).keyHandlerFromPacket(message.key);
				}
				else if (ridingEntity instanceof EntityRollingStock) {

					((EntityRollingStock) ridingEntity).keyHandlerFromPacket(message.key);
				}
				else if (ridingEntity instanceof AbstractZeppelin) {

					((AbstractZeppelin) ridingEntity).pressKey(message.key);
				}
				else if (ridingEntity instanceof EntityRotativeDigger) {

					((EntityRotativeDigger) ridingEntity).pressKey(message.key);
				}
			}

			return null;
		}
	}
}