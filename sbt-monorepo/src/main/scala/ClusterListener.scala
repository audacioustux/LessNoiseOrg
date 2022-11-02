import akka.actor.typed.*
import akka.actor.typed.scaladsl.*
import akka.cluster.ClusterEvent.*
import akka.cluster.MemberStatus
import akka.cluster.typed.*

object ClusterListener {

  sealed trait Event
  // internal adapted cluster events only
  private final case class ReachabilityChange(reachabilityEvent: ReachabilityEvent) extends Event
  private final case class MemberChange(event: MemberEvent)                         extends Event

  def apply(): Behavior[Event] = Behaviors.setup { ctx =>
    val memberEventAdapter: ActorRef[MemberEvent] = ctx.messageAdapter(MemberChange.apply)
    Cluster(ctx.system).subscriptions ! Subscribe(memberEventAdapter, classOf[MemberEvent])

    val reachabilityAdapter = ctx.messageAdapter(ReachabilityChange.apply)
    Cluster(ctx.system).subscriptions ! Subscribe(reachabilityAdapter, classOf[ReachabilityEvent])

    Behaviors.receiveMessage { message =>
      message match {
        case ReachabilityChange(reachabilityEvent) =>
          reachabilityEvent match {
            case UnreachableMember(member) =>
              ctx.log.info("Member detected as unreachable: {}", member)
            case ReachableMember(member) =>
              ctx.log.info("Member back to reachable: {}", member)
          }

        case MemberChange(changeEvent) =>
          changeEvent match {
            case MemberUp(member) =>
              ctx.log.info("Member is Up: {}", member.address)
            case MemberRemoved(member, previousStatus) =>
              ctx.log.info("Member is Removed: {} after {}", member.address, previousStatus)
            case _: MemberEvent => // ignore
          }
      }
      Behaviors.same
    }
  }
}
