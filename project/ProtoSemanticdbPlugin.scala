import sbt.{Compile, Def, _}
import sbt.Keys._
import sbt.plugins.JvmPlugin
import sbtprotoc.ProtocPlugin

object ProtoSemanticdbPlugin extends AutoPlugin {
  override def trigger = allRequirements
  override def requires = ProtocPlugin
  object autoImport {
    lazy val protocDescriptorFile = taskKey[File](
      "Location of the generated protobuf descriptor file"
    )
  }
  import autoImport._

  override lazy val projectSettings: Seq[Def.Setting[_]] = List(
    protocDescriptorFile := {
      val dir = semanticdbTargetRoot.in(Compile).value
      val result = dir / "META-INF" / "protobuf" / "protobuf.descriptor_set_out"
      result.mkdirs()
      result
    }
  )
}
