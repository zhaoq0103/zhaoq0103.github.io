
// ----------------------------------------------------------

var qview = null
var _onCurrentViewChangeds = []

function onCurrentViewChanged(handle) {
    _onCurrentViewChangeds.push(handle)
}

function setCurrentView(view) {
    let old = qview
    qview = view
    for (let i in _onCurrentViewChangeds) {
        let handle = _onCurrentViewChangeds[i]
        handle(old)
    }
}

function invalidate(reserved) {
    qview.invalidateRect(reserved)
}

// ----------------------------------------------------------

var _onViewAddeds = []

function onViewAdded(handle) {
    _onViewAddeds.push(handle)
}

function fireViewAdded(view) {
    for (let i in _onViewAddeds) {
        let handle = _onViewAddeds[i]
        handle(view)
    }
}

// ----------------------------------------------------------

class QPaintView {
  constructor(drawingID) {
    this.style = new QShapeStyle(1, "black", "white"); 
    this.controllers = {}
    this._currentKey = ""
    this._current = null
    this._selection = null
    this.onmousedown = null
    this.onmousemove = null
    this.onmouseup = null
    this.ondblclick = null
    this.onkeydown = null

    this.onSelectionChanged = null
    this.onControllerReset = null

    let drawing = document.getElementById(drawingID)
    let view = this

    drawing.onmousedown = function(event) {
      event.preventDefault()
      if (view.onmousedown != null) {
          view.onmousedown(event)
      }
    }
    drawing.onmousemove = function(event) {
        if (view.onmousemove != null) {
            // console.log("drawing.onmousemove")
            view.onmousemove(event)
        }
    }
    drawing.onmouseup = function(event) {
        if (view.onmouseup != null) {
            view.onmouseup(event)
        }
    }
    drawing.ondblclick = function(event) {
        event.preventDefault()
        if (view.ondblclick != null) {
            view.ondblclick(event)
        }
    }
    
    //注意是什么控件在捕获事件
    // drawing.onkeydown = function(event) {
    document.onkeydown = function(event) {
        switch (event.keyCode) {
          case 9: case 13: case 27:
              event.preventDefault()
        }
        if (view.onkeydown != null) {
            view.onkeydown(event)
        }
    }

    // 锚链接
    // URL 中 传值有两方式，一种是通过 search 来传值（即 ? 后面的部分），一种是通过 hash 来传值（即 # 后面的部分）
    // https://www.cnblogs.com/fly-allblue/p/3387334.html
    // 窗口导航到上一个或者一个新的URL时，会触发 onhashchange  

    window.onhashchange = function(event) {
      view.doc.reload()
      view.invalidateRect(null)
    }

    this.drawing = drawing
    this.doc = new QPaintDoc()
    this.doc.onload = function() {
      view.invalidateRect(null)
    }
    this.doc.init()
    // this.invalidateRect(null)
  }

  get currentKey() {
    return this._currentKey
  }

  get selection() {
    return this._selection
  }

  set selection(shape) {
    let old = this._selection
    if (old != shape) {
        this._selection = shape
        if (this.onSelectionChanged != null) {
            this.onSelectionChanged(old)
        }
    }
  }

  // get lineStyle() {
  //   let props = this.properties
  //   return new QLineStyle(props.lineWidth, props.lineColor)
  // }

  getMousePos(event) {
    return {
        x: event.offsetX,
        y: event.offsetY
    }
  }

  onpaint(ctx) {
    // 画出之前存储的图形
    this.doc.onpaint(ctx)

    //controller 画出当前的图形
    if (this._current != null) {
        this._current.onpaint(ctx)
    }
  }

  invalidateRect(reserved) {
    let ctx = this.drawing.getContext("2d")
    let bound = this.drawing.getBoundingClientRect()
    ctx.clearRect(0, 0, bound.width, bound.height)
    this.onpaint(ctx)
  }

  registerController(name, controller) {
    // console.log("view registerController " + name + " con:" + controller)

    if (name in this.controllers) {
        alert("Controller exists: " + name)
    } else {
        this.controllers[name] = controller
    }
  }

  invokeController(name) {
    // console.log("view invokeController " + name)

    this.stopController()
    if (name in this.controllers) {
        // 注意这里是调用的函数
        // let controller = this.controllers[name]
        // this._setCurrent(name, controller())
        let ctrl = this.controllers[name]
        this._setCurrent(name, ctrl())
    }
  }

  stopController() {
    if (this._current != null) {
        this._current.stop()
        this._setCurrent("", null)
    }
  }

  fireControllerReset() {
    if (this.onControllerReset != null) {
        this.onControllerReset()
    }
  }

  _setCurrent(name, ctrl) {
    // 每次点击相应的按钮或者鼠标动作，会invokeController, 设置当前的 current controller
    // console.log("view set current,name:"+name+" ctrl:"+ctrl)
    this._current = ctrl
    this._currentKey = name
  }

}

// ----------------------------------------------------------