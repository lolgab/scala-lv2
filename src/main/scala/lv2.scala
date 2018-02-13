import scala.scalanative.native._

@extern
object lv2 {
  type LV2_Handle = Ptr[Byte]

  type LV2_Feature = CStruct2[
    CString,
    Ptr[Byte]
  ]

  type _instantiateCallback = CFunctionPtr4[
    Ptr[Byte],
    CDouble,
    CString,
    Ptr[Ptr[LV2_Feature]],
    LV2_Handle
  ]

  type LV2_Descriptor = CStruct7[
    CString,
    _instantiateCallback,
    CFunctionPtr3[LV2_Handle, UInt, Ptr[Byte], Unit],
    CFunctionPtr1[LV2_Handle, Unit],
    CFunctionPtr2[LV2_Handle, UInt, Unit],
    CFunctionPtr1[LV2_Handle, Unit],
    CFunctionPtr1[LV2_Handle, Unit]
  ]

  def lv2_descriptor(index: UInt): Ptr[LV2_Descriptor] = extern

  type index                   = UInt
  type LV2_Descriptor_Function = CFunctionPtr1[index, Ptr[LV2_Descriptor]]

  type LV2_Lib_Handle = Ptr[Byte]

  type LV2_Lib_Descriptor = CStruct4[
    LV2_Lib_Handle,
    UInt,
    CFunctionPtr1[LV2_Lib_Handle, Unit],
    CFunctionPtr2[LV2_Lib_Handle, UInt, Ptr[LV2_Descriptor]]
  ]

  def lv2_lib_descriptor(bundlePath: CString,
                         features: Ptr[Ptr[LV2_Feature]]): Ptr[LV2_Lib_Descriptor] = extern
}

object lv2Ops {
  import lv2._

  implicit class LV2_FeatureOps(ptr: Ptr[LV2_Feature]) {
    def URI: CString    = !ptr._1
    def data: Ptr[Byte] = !ptr._2

    def URI_=(v: CString): Unit    = !ptr._1 = v
    def data_=(v: Ptr[Byte]): Unit = !ptr._2 = v
  }

  type bundle_path = CString
  type sample_rate = CDouble

  type instantiateCallback = CFunctionPtr4[
    Ptr[LV2_Descriptor],
    sample_rate,
    bundle_path,
    Ptr[Ptr[LV2_Feature]],
    LV2_Handle
  ]
  type sample_count = UInt
  type port         = UInt

  implicit class LV2_DescriptorOps(ptr: Ptr[LV2_Descriptor]) {
    def URI: CString                                                   = !ptr._1
    def instantiate: instantiateCallback                               = (!ptr._2).cast[instantiateCallback]
    def connect_port: CFunctionPtr3[LV2_Handle, port, Ptr[Byte], Unit] = !ptr._3
    def activate: CFunctionPtr1[LV2_Handle, Unit]                      = !ptr._4
    def run: CFunctionPtr2[LV2_Handle, sample_count, Unit]             = !ptr._5
    def deactivate: CFunctionPtr1[LV2_Handle, Unit]                    = !ptr._6
    def cleanup: CFunctionPtr1[LV2_Handle, Unit]                       = !ptr._7

    def URI_=(v: CString): Unit                                                   = !ptr._1 = v
    def instantiate_=(v: instantiateCallback): Unit                               = !ptr._2 = v.cast[_instantiateCallback]
    def connect_port_=(v: CFunctionPtr3[LV2_Handle, port, Ptr[Byte], Unit]): Unit = !ptr._3 = v
    def activate_=(v: CFunctionPtr1[LV2_Handle, Unit]): Unit                      = !ptr._4 = v
    def run_=(v: CFunctionPtr2[LV2_Handle, sample_count, Unit]): Unit             = !ptr._5 = v
    def deactivate_=(v: CFunctionPtr1[LV2_Handle, Unit]): Unit                    = !ptr._6 = v
    def cleanup_=(v: CFunctionPtr1[LV2_Handle, Unit]): Unit                       = !ptr._7 = v
  }

  implicit class LV2_Lib_DescriptorOps(ptr: Ptr[LV2_Lib_Descriptor]) {
    def handle: LV2_Lib_Handle                                               = !ptr._1
    def size: UInt                                                           = !ptr._2
    def cleanup: CFunctionPtr1[LV2_Lib_Handle, Unit]                         = !ptr._3
    def get_plugin: CFunctionPtr2[LV2_Lib_Handle, UInt, Ptr[LV2_Descriptor]] = !ptr._4

    def handle_=(v: LV2_Lib_Handle): Unit                       = !ptr._1 = v
    def size_=(v: UInt): Unit                                   = !ptr._2 = v
    def cleanup_=(v: CFunctionPtr1[LV2_Lib_Handle, Unit]): Unit = !ptr._3 = v
    def get_plugin_=(v: CFunctionPtr2[LV2_Lib_Handle, UInt, Ptr[LV2_Descriptor]]): Unit =
      !ptr._4 = v
  }
}
