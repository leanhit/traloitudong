  /**
   * Small description of your action
   * @title The title displayed in the flow editor
   * @category Custom
   * @author Your_Name
   * @param {string} name - An example string variable
   * @param {any} value - Another Example value
   */
  const myAction = async (name, value) => {
    event.state.session = event.state.session || {}
    event.state.session[name] = value
  }

  return myAction(args.name, args.value)