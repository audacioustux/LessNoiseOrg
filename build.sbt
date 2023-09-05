//

// common settings
inThisBuild(
  Seq(
    organization              := "com.audacioustux",
    scalaVersion              := "3.3.0",
    fork                      := true,
    Test / testForkedParallel := true,
    scalacOptions ++=
      Seq(
        "-explain",
        "-indent",
        "-rewrite",
        "-print-lines",
        "-deprecation",
        "-explain-types",
        "-feature",
        "-unchecked",
        "-Ykind-projector",
        "-Yimports",
        "-Wvalue-discard",
        "-Wunused:all",
        "-Xmigration",
      ),
  )
)

lazy val compaas = Project("compaas", file(".")).enablePlugins(JavaAppPackaging).settings(Dependencies.compaas)
